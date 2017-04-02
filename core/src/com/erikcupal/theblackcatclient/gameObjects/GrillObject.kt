package com.erikcupal.theblackcatclient.gameObjects

import com.badlogic.gdx.math.Vector2
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.gameObjects.HandObject
import com.erikcupal.theblackcatclient.types.CardTransformation
import com.erikcupal.theblackcatclient.types.CardValueChange
import com.erikcupal.theblackcatclient.types.PlayerSide

/**
 * Grill object that implements [[ICardsObject]]
 */
class GrillObject(side: PlayerSide): HandObject(side) {

  override val scale = CARD_INITIAL_SCALE * 0.85f
  override val relativeCardOffset = 1.1f

  override val relativeHandPositionX = 0.3f
  override val relativeHandPositionY = 0.26f

  override fun updateCard(index: Int, duration: Float, valueChange: CardValueChange?, noScaling: Boolean) {
    val card = cards[index]

    card.owner = side
    card.transform(
      transformation = CardTransformation(
        position = cardPosition(index),
        scale = Vector2(scale, scale),
        rotation = rotation,
        magnification = 1.3f,
        zIndex = 37 - index,
        noScaling = noScaling
      ),
      duration = duration,
      valueChange = valueChange
    )
  }

}