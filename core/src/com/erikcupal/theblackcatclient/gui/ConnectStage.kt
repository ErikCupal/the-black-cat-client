package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.*
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.types.*
import ktx.actors.alpha
import rx.lang.kotlin.plusAssign

class ConnectStage(game: GameCore) : StageBase(game) {

  private val background = Image(assets.getDrawable("connect_background"))
  private val servers = ServersGroup(game)
  private val rooms = RoomsGroup(game)
  private val errorMessage = ConnectionErrorMessageLabel(game)

  init {
    background.setSize(WIDTH, HEIGHT)

    listOf(background, servers, rooms).forEach {
      it.isVisible = false
      it.alpha = 0f
      this += it
    }

    this += errorMessage

    background.show(time = 1f)
    showGroup()

    configureMessageHandlers()
  }

  private fun showGroup() {
    when (state.name != null) {
      false -> servers.show(time = 1f)
      true  -> {
        rooms.show(time = 1f)
        send..GET_ROOMS()
      }
    }
  }

  private fun configureMessageHandlers() {
    subscription += messages.subscribe {
      when (it) {
        is REGISTERED        -> changeGroupToRooms()
        is ROOM_JOINED,
        is ROOM_CREATED      -> dispatch..GAME_SET_STAGE("GAME_STAGE")
        is GAME_LEAVE_SERVER -> changeGroupToServers()
      }
    }
  }

  private fun changeGroupToRooms() {
    servers.hide(time = 0.5f)
    this.addAction(delay(
      time = 0.5f,
      action = doAction {
        rooms.show(time = 0.5f)
      }
    ))
  }

  private fun changeGroupToServers() {
    rooms.hide(time = 0.5f)
    this.addAction(delay(
      time = 0.5f,
      action = doAction {
        servers.show(time = 0.5f)
      }
    ))
  }

  override fun dispose() {
    listOf(servers, rooms, errorMessage)
      .forEach(GroupBase::dispose)
    super.dispose()
  }
}