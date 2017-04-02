package com.erikcupal.theblackcatclient.gameObjects

import com.badlogic.gdx.math.Vector2
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.helpers.getPlayerRotation
import com.erikcupal.theblackcatclient.types.CardTransformation
import com.erikcupal.theblackcatclient.types.CardValueChange
import com.erikcupal.theblackcatclient.types.PlayerSide

/**
 * Hand object that implements [[ICardsObject]]
 */
open class HandObject(val side: PlayerSide) : ICardsObject {

  override var cards = mutableListOf<CardObject>()

  open val scale = CARD_INITIAL_SCALE
  val rotation = -3f + getPlayerRotation(side)
  open val relativeCardOffset = 0.5f
  open val magnification = when (side) {
    PlayerSide.BOTTOM -> 1.5f
    PlayerSide.LEFT,
    PlayerSide.RIGHT,
    PlayerSide.TOP    -> 1f
  }

  inline val cardWidth get() = scale * CARD_WIDTH
  inline val cardOffset get() = relativeCardOffset * cardWidth
  inline val handWidth get() = cardWidth + (cards.size - 1) * cardOffset

  open val relativeHandPositionX = 0.2f
  open val relativeHandPositionY = 0.12f

  inline val handPosition get() = Vector2(
    when (side) {
      PlayerSide.LEFT   -> WIDTH * relativeHandPositionX
      PlayerSide.RIGHT  -> WIDTH * (1 - relativeHandPositionX)
      PlayerSide.TOP    -> (CENTER.x + handWidth / 2)
      PlayerSide.BOTTOM -> (CENTER.x - handWidth / 2)
    },
    when (side) {
      PlayerSide.LEFT   -> (CENTER.y + handWidth / 2)
      PlayerSide.RIGHT  -> (CENTER.y - handWidth / 2)
      PlayerSide.TOP    -> HEIGHT * (1 - relativeHandPositionY)
      PlayerSide.BOTTOM -> HEIGHT * relativeHandPositionY
    }
  )

  fun cardPosition(index: Int) = Vector2(
    when (side) {
      PlayerSide.LEFT,
      PlayerSide.RIGHT  -> handPosition.x
      PlayerSide.TOP    -> handPosition.x - cardOffset * index
      PlayerSide.BOTTOM -> handPosition.x + cardOffset * index
    },
    when (side) {
      PlayerSide.LEFT   -> handPosition.y - cardOffset * index
      PlayerSide.RIGHT  -> handPosition.y + cardOffset * index
      PlayerSide.TOP,
      PlayerSide.BOTTOM -> handPosition.y
    }
  )

  override fun updateCard(index: Int, duration: Float, valueChange: CardValueChange?, noScaling: Boolean) {
    val card = cards[index]

    card.owner = side
    card.transform(
      transformation = CardTransformation(
        position = cardPosition(index),
        scale = Vector2(scale, scale),
        rotation = rotation,
        magnification = magnification,
        zIndex = 50 + index,
        noScaling = noScaling
      ),
      duration = duration,
      valueChange = valueChange
    )
  }

}