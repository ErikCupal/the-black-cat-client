package com.erikcupal.theblackcatclient.gameObjects

import com.badlogic.gdx.math.Vector2
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.gameObjects.CardObject
import com.erikcupal.theblackcatclient.helpers.minus
import com.erikcupal.theblackcatclient.types.CardTransformation
import com.erikcupal.theblackcatclient.types.CardValueChange

/**
 * Trick object that implements [[ICardsObject]]
 */
class TrickObject: ICardsObject {

  override var cards = mutableListOf<CardObject>()

  val scale = CARD_INITIAL_SCALE
  val rotation = -3f
  inline val cardWidth get() = scale * CARD_WIDTH
  inline val cardHeight get() = scale * CARD_HEIGHT
  inline val position get() = CENTER - Vector2(cardWidth /2, cardHeight /2)

  override fun updateCard(index: Int, duration: Float, valueChange: CardValueChange?, noScaling: Boolean) {

    val card = cards[index]

    card.transform(
      transformation = CardTransformation(
        position = position,
        scale = Vector2(scale, scale),
        rotation = rotation,
        magnification = 1f,
        zIndex = 10 + index,
        noScaling = noScaling
      ),
      duration = duration,
      valueChange = valueChange
    )
  }

}
