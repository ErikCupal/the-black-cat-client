package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.gui.PlayGroup
import com.erikcupal.theblackcatclient.gui.ScoresAndChatGroup
import com.erikcupal.theblackcatclient.gui.StageBase
import com.erikcupal.theblackcatclient.gui.GameMessageLabel
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.types.GAME_ENDED
import com.erikcupal.theblackcatclient.types.GAME_STARTED
import com.erikcupal.theblackcatclient.types.LEAVE_ROOM
import com.erikcupal.theblackcatclient.types.ROOM_LEFT
import rx.lang.kotlin.plusAssign
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

class GameStage(game: GameCore) : StageBase(game) {

  private val background = Image(assets.getDrawable("game_background"))
  private val overlay = Image(assets.getDrawable("black_overlay"))
  private val playGroup = PlayGroup(game)
  private val scoresAndChatGroup = ScoresAndChatGroup(game)
  private val switchGroupButton = TextButton("Table", assets.styles.mediumNoBackgroundButton)
  private val gameMessage = GameMessageLabel(game)

  init {
    background.setSize(WIDTH, HEIGHT)

    this += background
    this += playGroup
    this += scoresAndChatGroup
    this += switchGroupButton
    this += gameMessage
    this += overlay

    switchGroupButton.isTransform = true
    switchGroupButton.setPosition(100f, HEIGHT - 30f, Align.topLeft)

    configureButtonEventHandlers()
    configureMessageHandlers()
  }

  private fun configureButtonEventHandlers() {
    switchGroupButton onTap {
      when (switchGroupButton.text.toString()) {
        "Scores & Chat" -> {
          scoresAndChatGroup.show(time = 0.3f)
          switchGroupButton.setText("Table")
        }
        "Table"         -> {
          scoresAndChatGroup.hide(time = 0.3f)
          switchGroupButton.setText("Scores & Chat")
        }
      }
    }
  }

  private fun configureMessageHandlers() {
    subscription += messages.subscribe {
      when (it) {
        is GAME_STARTED -> switchGroupButton.setText("Scores & Chat")
        is GAME_ENDED,
        is ROOM_LEFT    -> switchGroupButton.setText("Table")
      }
    }
  }

  override fun dispose() {
    listOf(playGroup, scoresAndChatGroup, gameMessage).forEach(GroupBase::dispose)
    super.dispose()
  }

  override fun show() {
    overlay.hide(time = 1f, interpolation = fade)
  }

  override fun hide() {
    overlay.show(time = 1f, interpolation = fade)
  }
}