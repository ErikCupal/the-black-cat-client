package com.erikcupal.theblackcatclient.types

import com.badlogic.gdx.math.Vector2

/**
 * Shared game state
 */
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

data class AvailableRoom(
  val name: Name,
  val available: Boolean
)

interface ICard {
  val suit: Suit?
  val rank: Rank?
}

data class Card(
  override val suit: Suit,
  override val rank: Rank
) : ICard

val theBlackCatCard = Card(Suit.Spades, Rank.Queen)

enum class Suit { Clubs, Diamonds, Spades, Hearts }
enum class Rank { Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Ace }

typealias Name = String
typealias Id = String
typealias Number = Int
typealias Address = String
typealias BottomHand = MutableList<Card>
typealias HandOver = List<Card>
typealias Grill = List<Card>

enum class PlayerSide { LEFT, RIGHT, BOTTOM, TOP }

enum class AllowedCardSelection { HandOver, Grill }

/**
 * Tells whether card changed it's suit and rank
 */
data class CardValueChange(override val suit: Suit?, override val rank: Rank?) : ICard

/**
 * Is used for passing info about that how
 * card should be transformed
 */
data class CardTransformation(
  val position: Vector2,
  val scale: Vector2,
  val rotation: Float,
  val zIndex: Int,
  val magnification: Float = 1f,
  val noScaling: Boolean
)
