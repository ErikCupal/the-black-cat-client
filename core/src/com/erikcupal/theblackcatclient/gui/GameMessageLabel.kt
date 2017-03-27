package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.types.DEAL_DECK
import com.erikcupal.theblackcatclient.types.GAME_STARTED
import com.erikcupal.theblackcatclient.types.NEXT_ROUND
import com.erikcupal.theblackcatclient.types.NEXT_TURN
import rx.lang.kotlin.plusAssign
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

class GameMessageLabel(game: GameCore) : GroupBase(game) {

  val label = Label("", style.largeLabel)

  init {
    label.setAlignment(Align.center)
    positionCorrectly()
    this += label

    configureMessageHandlers()
  }

  private fun configureMessageHandlers() {
    var newRound = false

    subscription += messages.subscribe {
      when (it) {
        is GAME_STARTED -> changeMessage("Game started")
        is NEXT_ROUND   -> {
          changeMessage("New round")
          newRound = true
        }
        is NEXT_TURN    -> {
          val playerOnTurn = it.playerOnTurn

          if (newRound) {
            changeMessage(
              message = when (playerOnTurn) {
                state.name -> "It's your turn"
                else       -> "$playerOnTurn is on turn"
              }
            )
            newRound = false
          }
        }
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
    label addAction fadeOut(duration)
  }

  private fun showMessage(duration: Float = 0.3f) {
    label addAction fadeIn(duration)
  }

  private fun setText(text: String) {
    label.setText(text)
    positionCorrectly()
  }

  private fun positionCorrectly() {
    label.center()
  }
}
