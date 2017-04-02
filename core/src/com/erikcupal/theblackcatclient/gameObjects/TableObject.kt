package com.erikcupal.theblackcatclient.gameObjects

import com.badlogic.gdx.math.Vector2
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.helpers.getPlayerRotation
import com.erikcupal.theblackcatclient.helpers.plus
import com.erikcupal.theblackcatclient.types.CardTransformation
import com.erikcupal.theblackcatclient.types.CardValueChange
import com.erikcupal.theblackcatclient.types.PlayerSide

/**
 * Table object that implements [[ICardsObject]]
 */
class TableObject : ICardsObject {

  override var cards = mutableListOf<CardObject>()

  val scale = CARD_INITIAL_SCALE
  val relativeEccentricity = 3f / 4f
  val cardWidth = scale * CARD_WIDTH
  val cardHeight = scale * CARD_HEIGHT

  fun getCardRotation(side: PlayerSide) = -3f + getPlayerRotation(side)

  fun getCardPosition(side: PlayerSide) = CENTER + Vector2(
    when (side) {
      PlayerSide.BOTTOM -> -(cardWidth / 2)
      PlayerSide.TOP    -> (cardWidth / 2)
      PlayerSide.LEFT   -> -(cardHeight * relativeEccentricity)
      PlayerSide.RIGHT  -> (cardHeight * relativeEccentricity)
    },
    when (side) {
      PlayerSide.BOTTOM -> -(cardHeight * relativeEccentricity)
      PlayerSide.TOP    -> (cardHeight * relativeEccentricity)
      PlayerSide.LEFT   -> (cardWidth / 2)
      PlayerSide.RIGHT  -> -(cardWidth / 2)
    }
  )

  override fun updateCard(index: Int, duration: Float, valueChange: CardValueChange?, noScaling: Boolean) {

    val card = cards[index]

    card.transform(
      transformation = CardTransformation(
        position = getCardPosition(card.owner!!),
        scale = Vector2(scale, scale),
        rotation = getCardRotation(card.owner!!),
        magnification = 1f,
        zIndex = 50 + index,
        noScaling = noScaling
      ),
      duration = duration,
      valueChange = valueChange
    )
  }

}
