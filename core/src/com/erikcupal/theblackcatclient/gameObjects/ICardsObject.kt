package com.erikcupal.theblackcatclient.gameObjects

import com.erikcupal.theblackcatclient.types.CardValueChange

interface ICardsObject {

  var cards: MutableList<CardObject>

  fun updateCard(index: Int, duration: Float, valueChange: CardValueChange? = null, noScaling: Boolean = false)

  fun updateCards(duration: Float, except: CardObject? = null) {
    cards.forEachIndexed { index, card ->
      if (card !== except) {
        updateCard(index, duration, noScaling = true)
      }
    }
  }

  operator fun plusAssign(card: CardObject) {
    cards.add(card)
  }

  operator fun minusAssign(card: CardObject) {
    cards.remove(card)
  }

}