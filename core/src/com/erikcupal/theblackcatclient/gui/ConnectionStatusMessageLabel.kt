package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.CENTER
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.HEIGHT
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.types.*
import rx.lang.kotlin.plusAssign

/**
 * Connection message label used in [[ConnectStage]]
 */
class ConnectionStatusMessageLabel(game: GameCore) : GroupBase(game) {

  val infoLabel = Label("", style.errorLabel)

  init {
    infoLabel.setAlignment(Align.center)
    positionCorrectly()
    this += infoLabel

    configureMessageHandlers()
  }

  private fun configureMessageHandlers() {

    subscription += messages.subscribe {
      when (it) {
        is ROOM_NAME_TAKEN,
        is NAME_TAKEN         -> changeMessage("Name taken! Choose other name.")
        is GAME_NAME_TOO_LONG -> changeMessage("Name is too long! Choose other name.")
        is CONNECT_FAILED     -> changeMessage("Failed to connect to server. Check for typos.")
        is CONNECTING         -> changeMessage("Connecting...")
        is SERVER_FULL        -> changeMessage("Server is full. Try later.")
      }
    }
  }

  private fun changeMessage(message: String) {
    setText(message)
    showMessage()
    this addAction delay(1f,
      doAction {
        hideMessage()
      }
    )
  }

  private fun hideMessage(duration: Float = 0.3f) {
    infoLabel addAction fadeOut(duration)
  }

  private fun showMessage(duration: Float = 0.3f) {
    infoLabel addAction fadeIn(duration)
  }

  private fun setText(text: String) {
    infoLabel.setText(text)
    positionCorrectly()
  }

  private fun positionCorrectly() {
    infoLabel.setPosition(CENTER.x, HEIGHT * 0.25f, Align.center)
  }
}