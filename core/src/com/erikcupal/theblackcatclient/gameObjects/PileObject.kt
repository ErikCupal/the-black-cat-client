package com.erikcupal.theblackcatclient.gameObjects

import com.badlogic.gdx.math.Vector2
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.gameObjects.CardObject
import com.erikcupal.theblackcatclient.helpers.minus
import com.erikcupal.theblackcatclient.helpers.playerRotation
import com.erikcupal.theblackcatclient.types.CardTransformation
import com.erikcupal.theblackcatclient.types.CardValueChange
import com.erikcupal.theblackcatclient.types.PlayerSide

class PileObject(val side: PlayerSide) : ICardsObject {

  override var cards = mutableListOf<CardObject>()

  val BOTTOM_PILE_POSITION = Vector2(1400f, 170f)
  val LEFT_PILE_POSITION = Vector2(450f, 230f)

  val scale = CARD_INITIAL_SCALE * 0.9f
  val rotation = -80f + playerRotation(side)
  val position = when (side) {
    PlayerSide.BOTTOM -> BOTTOM_PILE_POSITION
    PlayerSide.TOP    -> GAME_SIZE - BOTTOM_PILE_POSITION - BOTTOM_TABLE_OFFSET
    PlayerSide.LEFT   -> LEFT_PILE_POSITION
    PlayerSide.RIGHT  -> GAME_SIZE - LEFT_PILE_POSITION - BOTTOM_TABLE_OFFSET
  }

  override fun updateCard(index: Int, duration: Float, valueChange: CardValueChange?, noScaling: Boolean) {
    val card = cards[index]

    card.owner = side
    card.transform(
      transformation = CardTransformation(
        position = position,
        scale = Vector2(scale, scale),
        rotation = rotation,
        magnification = 1f,
        zIndex = index,
        noScaling = noScaling
      ),
      duration = duration,
      valueChange = valueChange
    )
  }

}