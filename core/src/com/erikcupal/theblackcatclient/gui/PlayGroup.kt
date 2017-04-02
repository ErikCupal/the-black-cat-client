package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.gameObjects.CardsContainer
import com.erikcupal.theblackcatclient.helpers.getPlayerSide
import com.erikcupal.theblackcatclient.helpers.plusAssign
import com.erikcupal.theblackcatclient.types.PLAYER_JOINED
import com.erikcupal.theblackcatclient.types.PLAYER_REPLACED_WITH_BOT
import com.erikcupal.theblackcatclient.types.PlayerSide
import rx.lang.kotlin.plusAssign

/**
 * Group with table picture, cards and gameplay buttons
 */
class PlayGroup(game: GameCore) : GroupBase(game) {
  private val tableImage = Image(assets.getDrawable("table"))
  private val gameButtons = GameButtonsGroup(game)
  private val cardsGroup = CardsContainer(game)

  // Player labels
  val leftPlayerLabel = PlayerNameLabel("", style.smallLabel)
  val topPlayerLabel = PlayerNameLabel("", style.smallLabel)
  val rightPlayerLabel = PlayerNameLabel("", style.smallLabel)

  init {
    this += tableImage
    this += cardsGroup
    this += gameButtons

    tableImage.setPosition(300f, 35f)
    tableImage.width = WIDTH - 600
    tableImage.height = HEIGHT - 100

    addPlayerLabels()
    positionPlayerLabels()
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

  private fun positionPlayerLabels() {
    topPlayerLabel.setPosition(CENTER.x, HEIGHT - 46f, Align.center)
    leftPlayerLabel.setPosition(280f, CENTER.y, Align.center)
    rightPlayerLabel.setPosition(WIDTH - 280f, CENTER.y, Align.center)
    leftPlayerLabel.rotation = 90f
    rightPlayerLabel.rotation = -90f
  }

  private fun addPlayerLabels() {
    this += leftPlayerLabel
    this += topPlayerLabel
    this += rightPlayerLabel
  }

  fun updatePlayerLabels() {
    state.players?.forEach { name ->
      val side = state.getPlayerSide(name)
      when (side) {
        PlayerSide.LEFT  -> leftPlayerLabel.text = name
        PlayerSide.TOP   -> topPlayerLabel.text = name
        PlayerSide.RIGHT -> rightPlayerLabel.text = name
      }
    }
  }

  override fun dispose() {
    gameButtons.dispose()
    cardsGroup.dispose()
    super.dispose()
  }
}