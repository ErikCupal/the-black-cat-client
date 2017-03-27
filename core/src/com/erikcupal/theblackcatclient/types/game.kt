package com.erikcupal.theblackcatclient.types

import com.badlogic.gdx.math.Vector2

data class State(
  var name: Name? = null,
  var room: Name? = null,
  var players: MutableList<Name>? = null,
  var handOver: HandOver? = null
)

data class LastPlayerScore(
  val player: Name,
  val points: Number
)

interface ICard {
  val suit: Suit?
  val rank: Rank?
}

data class Card(
  override val suit: Suit,
  override val rank: Rank
) : ICard

val theBlackCat = Card(Suit.Spades, Rank.Queen)

enum class Suit { Clubs, Diamonds, Spades, Hearts }
enum class Rank { Seven, Eight, Nine, Ten, Jack, Queen, King, Ace }

typealias Name = String
typealias Id = String
typealias Number = Int
typealias Address = String
typealias BottomHand = MutableList<Card>
typealias HandOver = List<Card>
typealias Grill = List<Card>

enum class PlayerSide { LEFT, RIGHT, BOTTOM, TOP }

enum class AllowedCardSelection { HandOver, Grill }

data class CardValueChange(override val suit: Suit?, override val rank: Rank?) : ICard

data class CardTransformation(
  val position: Vector2,
  val scale: Vector2,
  val rotation: Float,
  val zIndex: Int,
  val magnification: Float = 1f,
  val noScaling: Boolean
)
