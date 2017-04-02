package com.erikcupal.theblackcatclient.core

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.erikcupal.theblackcatclient.assets.Assets
import com.erikcupal.theblackcatclient.gui.ConnectStage
import com.erikcupal.theblackcatclient.gui.GameStage
import com.erikcupal.theblackcatclient.gui.SplashStage
import com.erikcupal.theblackcatclient.gui.StageBase
import com.erikcupal.theblackcatclient.helpers.clearScreen
import com.erikcupal.theblackcatclient.helpers.doAction
import com.erikcupal.theblackcatclient.helpers.log
import com.erikcupal.theblackcatclient.state.stateUpdaterFactory
import com.erikcupal.theblackcatclient.types.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.Socket.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.json.JSONObject
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

/**
 * GameCore
 *
 * Main game class.
 * Handles rendering, screen changing
 * dispatching game and state messages
 * and send/receiving client/server messages.
 *
 * @property jsonMapper serializes and deserializes JSON strings from/to Kotlin data classes
 * @property messages RxKotlin Subject - it is used as messaging hub
 * @property dispatch dispatches game and state messages
 * @property send sends/receives client/server messages
 * @property socket Socket.IO Socket object - communicates with server over WebSockets
 * @property socketOptions Socket.IO options object - it is necessary in order to support SSL/TLS
 * @property batch SpriteBatch used for rendering graphics
 * @property stage Scene2D stage - used for drawing Actors on [batch]
 */
class GameCore : ApplicationAdapter() {

  private val jsonMapper = jacksonObjectMapper()
  val assets = Assets()
  val state = State()
  private val updateState = stateUpdaterFactory(state)
  val messages = SerializedSubject(PublishSubject.create<Any>())
  val dispatch = Dispatch { message -> messages.onNext(message) }
  val send = Send { payload -> sendMessageToServer(payload) }

  private var socket: Socket? = null
  private val socketOptions = createSocketIoOptions()

  lateinit var batch: SpriteBatch

  val multiplexer = InputMultiplexer()
  val keyboardShortcutsListener = KeyboardShortcutsListener()

  private lateinit var stage: StageBase

  override fun create() {
    batch = SpriteBatch()
    configureMessageHandlers()

    Gdx.input.inputProcessor = multiplexer
    multiplexer.addProcessor(keyboardShortcutsListener)

    stage = SplashStage(this)
  }

  override fun render() {
    clearScreen()

    val deltaTime = Gdx.graphics.deltaTime
    stage.act(deltaTime)
    stage.draw()
  }

  override fun resize(width: Int, height: Int) {
    stage.viewport?.update(width, height, true)
  }

  override fun dispose() {
    stage.dispose()
    batch.dispose()
    assets.dispose()
  }

  private fun configureMessageHandlers() {
    messages.subscribe { message ->
      log("🏓 DISPATCHING MESSAGE", "$message")

      updateState(message)

      when (message) {
        is CONNECT           -> initializeSocket(message.server)
        is GAME_SET_STAGE    -> changeStage(message.stageName)
        is GAME_LEAVE_SERVER -> socket?.disconnect()
      }
    }
  }

  private fun changeStage(stageName: Name) {
    stage.hide()

    stage.addAction(com.erikcupal.theblackcatclient.helpers.delay(
      time = 1f,
      action = doAction {
        multiplexer.removeProcessor(stage)
        stage.dispose()
        stage = when (stageName) {
          "CONNECT_STAGE" -> ConnectStage(this)
          "GAME_STAGE"    -> GameStage(this)
          else            -> throw error("Invalid stage")
        }
        multiplexer.addProcessor(stage)
        stage.show()
      }
    ))
  }

  fun isConnected() = socket != null

  private fun initializeSocket(server: String) {
    try {
      socket = IO.socket(server, socketOptions)
      socket?.connect()

      socket?.on(EVENT_CONNECT) {
        log("🕊️ socket CONNECTED", server)
        dispatch..CONNECTED()
      }
      socket?.on(EVENT_CONNECTING) {
        log("🕊️ socket CONNECTING", server)
        dispatch..CONNECTING()
      }
      socket?.on(EVENT_DISCONNECT) {
        log("🕊️ socket DISCONNECTED", server)
        socket = null
        dispatch..DISCONNECTED()
      }
      socket?.on("MESSAGE") { data ->
        onSocketMessage(data)
      }

      socket?.on(EVENT_CONNECT_ERROR) {
        log("🕊️ socket CONNECT ERROR", server)
        dispatch..CONNECT_FAILED()
        socket = null
      }
    } catch (e: Exception) {
      log("🕊️🕊️🕊️ SOCKET FAILED TO CONNECT - INVALID SERVER ADDRESS 🕊️🕊️🕊️")
      dispatch..CONNECT_FAILED()
    }
  }

  private fun sendMessageToServer(payload: Any) {
    if (socket != null && socket!!.connected()) {
      launch(CommonPool) {
        val jsonMessage = """{"type": "${payload::class.simpleName}",""" + jsonMapper.writeValueAsString(payload).removeRange(0..0)
        socket?.emit("MESSAGE", jsonMessage)
        log("🦄 SOCKET_MESSAGE_SEND", payload.toString())
      }
    }
  }

  private fun onSocketMessage(data: Array<Any>) {
    if (data.isNotEmpty()) {

      val socketMessage = data[0] as JSONObject
      val messageType = socketMessage.getString("type")
      socketMessage.remove("type")
      val jsonPayload = socketMessage.toString()

      val deserializedMessage: Any? = when (messageType) {
        CONNECTION_CONFIRMATION::class.simpleName  -> jsonMapper.readValue<CONNECTION_CONFIRMATION>(jsonPayload)
        REGISTERED::class.simpleName               -> jsonMapper.readValue<REGISTERED>(jsonPayload)
        NAME_TAKEN::class.simpleName               -> jsonMapper.readValue<NAME_TAKEN>(jsonPayload)
        ROOM_CREATED::class.simpleName             -> jsonMapper.readValue<ROOM_CREATED>(jsonPayload)
        ROOM_NAME_TAKEN::class.simpleName          -> jsonMapper.readValue<ROOM_NAME_TAKEN>(jsonPayload)
        ROOM_JOINED::class.simpleName              -> jsonMapper.readValue<ROOM_JOINED>(jsonPayload)
        ROOM_FULL::class.simpleName                -> jsonMapper.readValue<ROOM_FULL>(jsonPayload)
        ROOM_LEFT::class.simpleName                -> jsonMapper.readValue<ROOM_LEFT>(jsonPayload)
        PLAYER_JOINED::class.simpleName            -> jsonMapper.readValue<PLAYER_JOINED>(jsonPayload)
        GAME_STARTED::class.simpleName             -> jsonMapper.readValue<GAME_STARTED>(jsonPayload)
        GAME_ENDED::class.simpleName               -> jsonMapper.readValue<GAME_ENDED>(jsonPayload)
        PLAYER_REPLACED_WITH_BOT::class.simpleName -> jsonMapper.readValue<PLAYER_REPLACED_WITH_BOT>(jsonPayload)
        NEXT_ROUND::class.simpleName               -> jsonMapper.readValue<NEXT_ROUND>(jsonPayload)
        NEXT_TURN::class.simpleName                -> jsonMapper.readValue<NEXT_TURN>(jsonPayload)
        DEAL_DECK::class.simpleName                -> jsonMapper.readValue<DEAL_DECK>(jsonPayload)
        DO_PASS_HANDOVER::class.simpleName         -> jsonMapper.readValue<DO_PASS_HANDOVER>(jsonPayload)
        HAND_OVER_PLAYED::class.simpleName         -> jsonMapper.readValue<HAND_OVER_PLAYED>(jsonPayload)
        HAND_OVER_TAKEN::class.simpleName          -> jsonMapper.readValue<HAND_OVER_TAKEN>(jsonPayload)
        TRICK_COLLECTED::class.simpleName          -> jsonMapper.readValue<TRICK_COLLECTED>(jsonPayload)
        TRICK_FINISHED::class.simpleName           -> jsonMapper.readValue<TRICK_FINISHED>(jsonPayload)
        DO_TAKE_HANDOVER::class.simpleName         -> jsonMapper.readValue<DO_TAKE_HANDOVER>(jsonPayload)
        GRILL_PLAYED::class.simpleName             -> jsonMapper.readValue<GRILL_PLAYED>(jsonPayload)
        CARD_PLAYED::class.simpleName              -> jsonMapper.readValue<CARD_PLAYED>(jsonPayload)
        AVAILABLE_ROOMS::class.simpleName          -> jsonMapper.readValue<AVAILABLE_ROOMS>(jsonPayload)
        UPDATED_SCORES::class.simpleName           -> jsonMapper.readValue<UPDATED_SCORES>(jsonPayload)
        CHAT_MESSAGE::class.simpleName             -> jsonMapper.readValue<CHAT_MESSAGE>(jsonPayload)
        else                                       -> null
      }

      if (deserializedMessage != null) {
        Gdx.app.postRunnable {
          messages.onNext(deserializedMessage)
        }
      } else {
        log("🕊️🕊️🕊️ SOCKET MESSAGE TYPE NOT RECOGNIZED 🕊️🕊️🕊️", messageType)
      }
    }
  }

  /**
   * Used for enabling syntax
   * dispatch..MESSAGE() instead of dispatch(MESSAGE())
   */
  class Dispatch(private val callback: (Any) -> Unit) {
    operator fun rangeTo(message: Any) = callback(message)
  }

  /**
   * Used for enabling syntax
   * send..MESSAGE() instead of send(MESSAGE())
   */
  class Send(private val callback: (Any) -> Unit) {
    operator fun rangeTo(payload: Any) = callback(payload)
  }
}