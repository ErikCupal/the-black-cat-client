package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.GameCore
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.types.PLAYER_JOINED
import com.erikcupal.theblackcatclient.types.PLAYER_REPLACED_WITH_BOT
import rx.lang.kotlin.plusAssign

class ScoresHeaderGroup(game: GameCore) : GroupBase(game) {

  val header = Table()

  val gameLabel = Label("Game", style.scoresHeaderLabel)
  val playerLabel1 = Label("", style.scoresHeaderLabel)
  val playerLabel2 = Label("", style.scoresHeaderLabel)
  val playerLabel3 = Label("", style.scoresHeaderLabel)
  val playerLabel4 = Label("", style.scoresHeaderLabel)

  init {
    header.width = 1300f
    header.setPosition(100f, 855f, Align.bottomLeft)

    this.addActor(header)

    listOf(
      gameLabel,
      playerLabel1,
      playerLabel2,
      playerLabel3,
      playerLabel4
    ).forEachIndexed { index, label ->
      label.setAlignment(Align.center)
      label.setEllipsis(true)

      val isGameLabel = index == 0
      when (isGameLabel) {
        true  -> header.add(gameLabel).width(170f)
        false -> header.add(label).width(240f).pad(15f)
      }
    }

    updatePlayerLabels()
    configureMessageHandlers()
  }

  private fun configureMessageHandlers() {
    subscription += messages.subscribe {
      when (it) {
        is PLAYER_JOINED,
        is PLAYER_REPLACED_WITH_BOT -> updatePlayerLabels()
      }
    }
  }

  private fun updatePlayerLabels() {
    state.players?.forEachIndexed { index, name ->
      when (index) {
        0 -> playerLabel1.setText(name)
        1 -> playerLabel2.setText(name)
        2 -> playerLabel3.setText(name)
        3 -> playerLabel4.setText(name)
      }
    }
  }
}