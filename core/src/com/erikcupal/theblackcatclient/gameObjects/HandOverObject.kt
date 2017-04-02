package com.erikcupal.theblackcatclient.gameObjects

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.helpers.minus
import com.erikcupal.theblackcatclient.helpers.getPlayerRotation
import com.erikcupal.theblackcatclient.helpers.plus
import com.erikcupal.theblackcatclient.types.CardTransformation
import com.erikcupal.theblackcatclient.types.CardValueChange
import com.erikcupal.theblackcatclient.types.PlayerSide

/**
 * HandOver object that implements [[ICardsObject]]
 */
class HandOverObject(val side: PlayerSide) : ICardsObject {

  override var cards = mutableListOf<CardObject>()

  val BOTTOM_HANDOVER_POSITION = Vector2(1300f, 130f)
  val LEFT_HANDOVER_POSITION = Vector2(380f, 250f)

  val scale = CARD_INITIAL_SCALE * 0.9f
  val relativeOffset = 0.4f
  val cardWidth = scale * CARD_WIDTH
  val offsetLength = cardWidth * relativeOffset
  val rotation = 10f + getPlayerRotation(side)
  val cardRotation = 5f + getPlayerRotation(side)

  fun cardPosition(index: Int) =
    Vector2(
      MathUtils.cosDeg(rotation) * offsetLength * index,
      MathUtils.sinDeg(rotation) * offsetLength * index
    ) + when (side) {
      PlayerSide.BOTTOM -> BOTTOM_HANDOVER_POSITION
      PlayerSide.TOP    -> GAME_SIZE - BOTTOM_HANDOVER_POSITION - BOTTOM_TABLE_OFFSET
      PlayerSide.LEFT   -> LEFT_HANDOVER_POSITION
      PlayerSide.RIGHT  -> GAME_SIZE - LEFT_HANDOVER_POSITION - BOTTOM_TABLE_OFFSET
    }

  override fun updateCard(index: Int, duration: Float, valueChange: CardValueChange?, noScaling: Boolean) {
    val card = cards[index]

    card.owner = side
    card.transform(
      transformation = CardTransformation(
        position = cardPosition(index),
        scale = Vector2(scale, scale),
        rotation = cardRotation,
        magnification = 1.5f,
        zIndex = 40 + index,
        noScaling = noScaling
      ),
      duration = duration,
      valueChange = valueChange
    )
  }
}