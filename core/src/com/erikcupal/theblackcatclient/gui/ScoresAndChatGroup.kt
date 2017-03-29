package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.helpers.hide
import com.erikcupal.theblackcatclient.helpers.onTap
import com.erikcupal.theblackcatclient.helpers.plusAssign
import com.erikcupal.theblackcatclient.helpers.show
import com.erikcupal.theblackcatclient.types.*
import ktx.actors.alpha

class ScoresAndChatGroup(game: GameCore) : GroupBase(game) {

  val background = Image(assets.getDrawable("game_background"))

  // Buttons
  val leaveRoomButton = createButton("Leave room", style.button)
  val newGameButton = createButton("New game", style.button)
  val addBotButton = createButton("Add bot", style.button)

  val playersLabel = Label("Players: ${getPlayersCount()}/4", style.mediumLabel)

  val scoresBox = ScoresGroup(game)
  val chatBox = ChatGroup(game)

  init {

    this += background

    this += newGameButton
    this += addBotButton
    this += leaveRoomButton

    this += playersLabel

    this += scoresBox
    this += chatBox

    messages.subscribe {
      when (it) {
        is GAME_STARTED  -> {
          hide(time = 1f)
          newGameButton.hide()
        }
        is GAME_ENDED    -> {
          show(time = 1f)
          newGameButton.show()
        }
        is PLAYER_JOINED -> {
          val playersCount = getPlayersCount()
          playersLabel.setText("Players: $playersCount/4")

          if (playersCount == 4) {
            addBotButton.hide()
          }
        }
      }
    }

    newGameButton onTap {
      send..I_WANT_NEW_GAME()
    }

    addBotButton onTap {
      send..ADD_BOT()
    }

    leaveRoomButton onTap {
      send..LEAVE_ROOM()
      dispatch..GAME_SET_STAGE("CONNECT_STAGE")
    }

    playersLabel.setPosition(600f, 35f, Align.bottom)

    leaveRoomButton.setPosition(140f, 20f)
    leaveRoomButton.show()

    addBotButton.setPosition(770f, 20f)
    addBotButton.show()

    newGameButton.setPosition(1080f, 20f)

    background.zIndex = 0
  }

  private fun createButton(text: String, style: TextButton.TextButtonStyle, position: Vector2 = Vector2(0f, 0f)): TextButton {
    val button = TextButton(text, style)
    button.isTransform = true
    button.setPosition(position.x, position.y)
    button.alpha = 0f
    button.height = 70f
    button.isVisible = false
    this += button
    return button
  }

  private fun getPlayersCount(): Int = state.players?.size ?: 1

}
