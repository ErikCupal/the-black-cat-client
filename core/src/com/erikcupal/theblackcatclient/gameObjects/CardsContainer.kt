package com.erikcupal.theblackcatclient.gameObjects

import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.types.*
import rx.lang.kotlin.plusAssign

/**
 * Contains all [[CardObject]] objects and [[ICardsObject]] objects like hand, grill, etc.
 *
 * Transforms cards from one [[ICardsObject]] to another.
 */
class CardsContainer(game: GameCore) : GroupBase(game) {

  val cards = List(32) { CardObject(game) }

  // Card containers

  val deck = DeckObject()
  val table = TableObject()
  val trick = TrickObject()

  val bottomHand = HandObject(PlayerSide.BOTTOM)
  val topHand = HandObject(PlayerSide.TOP)
  val leftHand = HandObject(PlayerSide.LEFT)
  val rightHand = HandObject(PlayerSide.RIGHT)

  val bottomGrill = GrillObject(PlayerSide.BOTTOM)
  val topGrill = GrillObject(PlayerSide.TOP)
  val leftGrill = GrillObject(PlayerSide.LEFT)
  val rightGrill = GrillObject(PlayerSide.RIGHT)

  val bottomHandOver = HandOverObject(PlayerSide.BOTTOM)
  val topHandOver = HandOverObject(PlayerSide.TOP)
  val leftHandOver = HandOverObject(PlayerSide.LEFT)
  val rightHandOver = HandOverObject(PlayerSide.RIGHT)

  val bottomPile = PileObject(PlayerSide.BOTTOM)
  val topPile = PileObject(PlayerSide.TOP)
  val leftPile = PileObject(PlayerSide.LEFT)
  val rightPile = PileObject(PlayerSide.RIGHT)

  var allowedCardSelection: AllowedCardSelection? = null
    set(value) {
      when (value) {
        AllowedCardSelection.HandOver -> allowHandoverSelection()
        AllowedCardSelection.Grill    -> allowGrillSelection()
        null                          -> disallowSelection(cards)
      }
      field = value
    }

  inline val selectedCards get() = cards.filter { it.isSelected }

  init {
    cards.forEach { card ->
      this += card
    }
    cardsToDeck(duration = 0f)

    configureMessageHandlers()
  }

  private fun configureMessageHandlers() {
    subscription += messages.subscribe {
      when (it) {
        is GAME_CARD_SELECTION_CHANGED -> {
          when (allowedCardSelection) {
            AllowedCardSelection.HandOver -> {
              dispatch..GAME_HANDOVER_SELECTION_CHANGED(canCardsBeSend())
            }
            AllowedCardSelection.Grill    -> {
              dispatch..GAME_GRILL_SELECTION_CHANGED(canCardsBeSend())
            }
            null                          -> {
              dispatch..GAME_HANDOVER_SELECTION_CHANGED(readyToSend = false)
              dispatch..GAME_GRILL_SELECTION_CHANGED(readyToSend = false)
            }
          }
        }
        is SORT_HAND_BUTTON            -> sortHand()
        is DEAL_DECK                   -> dealCards(it.hand)
        is DO_PASS_HANDOVER            -> allowedCardSelection = AllowedCardSelection.HandOver
        is PASS_CARDS_BUTTON           -> {
          val handOver = selectedCards.map { Card(it.suit!!, it.rank!!) }
          passHandOver(handOver, from = PlayerSide.BOTTOM)
          disallowSelection(selectedCards)

          send..PLAY_HAND_OVER(handOver)
        }
        is GRILL_CARDS_BUTTON          -> {
          val grill = selectedCards.map { Card(it.suit!!, it.rank!!) }
          grillCards(grill, player = PlayerSide.BOTTOM)
          disallowSelection(selectedCards)

          send..PLAY_GRILL(grill)
        }
        is HAND_OVER_PLAYED            -> {
          val side = state.getPlayerSide(it.from)
          passHandOver(from = side)
        }
        is DO_TAKE_HANDOVER            -> allowedCardSelection = null
        is TAKE_CARDS_BUTTON           -> {
          takeHandOver(state.handOver!!, player = PlayerSide.BOTTOM)
          allowedCardSelection = AllowedCardSelection.Grill
          send..TAKE_HANDOVER()
        }
        is HAND_OVER_TAKEN             -> {
          val side = state.getPlayerSide(it.player)
          takeHandOver(player = side)
        }
        is GRILL_PLAYED                -> {
          val side = state.getPlayerSide(it.player)
          grillCards(it.grill, player = side)
        }
        is READY_BUTTON                -> {
          allowedCardSelection = null
          send..I_AM_READY()
        }
        is NEXT_TURN                   -> {
          if (it.playerOnTurn == state.name) {
            (bottomHand.cards + bottomGrill.cards).forEach { card ->
              card.canBePlayed = cardIsPlayable(card)
            }
          }
        }
        is GAME_PLAY_CARD              -> {
          val card = it.card
          val fromGrill: Boolean = bottomGrill.cards.find(isSameCard(card)) != null

          playCard(
            card = Card(
              suit = card.suit!!,
              rank = card.rank!!
            ),
            player = PlayerSide.BOTTOM,
            fromGrill = fromGrill
          )

          cards.forEach { it.canBePlayed = null }

          send..PLAY_CARD(Card(
            suit = card.suit!!,
            rank = card.rank!!
          ))
        }
        is CARD_PLAYED                 -> {
          val side = state.getPlayerSide(player = it.player)
          playCard(
            card = it.card,
            player = side,
            fromGrill = it.cardFromGrill
          )
        }
        is TRICK_FINISHED              -> {
          collectTrick()
          this addAction delay(
            time = 1.2f,
            action = doAction {
              val side = state.getPlayerSide(it.receiver)
              trickToPile(side)
            }
          )
        }
        is GAME_ENDED                  -> cardsToDeck()
      }
    }
  }

  val isSameCard: (ICard) -> (ICard) -> Boolean = { a -> { b -> a.suit == b.suit && a.rank == b.rank } }
  val isOfSuit: (Suit?) -> (ICard) -> Boolean = { suit -> { card -> card.suit == suit } }
  val isBlackCat: (ICard) -> Boolean = isSameCard(theBlackCatCard)

  fun cardIsPlayable(card: ICard) = fun(): Boolean {
    val firstTableCard = table.cards.getOrNull(0)
    if (firstTableCard == null || card.suit == firstTableCard.suit) {
      return true
    }
    val cardOfSameSuit = (bottomHand.cards + bottomGrill.cards).find(isOfSuit(firstTableCard.suit))
    if (cardOfSameSuit == null) {
      return true
    }
    return false
  }

  private fun sortHand() {
    bottomHand.cards.sortWith(compareBy({ it.suit }, { it.rank }))
    bottomHand.cards.clone().forEach { card ->
      transformCard(
        card = card,
        from = bottomHand,
        to = bottomHand,
        duration = 1f
      )
    }
  }

  private fun allowHandoverSelection() {
    bottomHand.cards.forEach { card ->
      card.canBeSelected = fun(): Boolean {
        val selectableCardsCount = 3 - bottomHand.cards.filter { it.isSelected }.size
        return selectableCardsCount > 0
      }
    }
  }

  private fun allowGrillSelection() {
    bottomHand.cards.forEach { card ->
      card.canBeSelected =
        fun(): Boolean =
          when (card.suit) {
            Suit.Hearts -> true
            Suit.Spades -> when (card.rank) {
              Rank.Queen -> true
              else       -> false
            }
            else        -> false
          }
    }
  }

  private fun disallowSelection(cards: List<CardObject>) {
    cards.forEach {
      it.isSelected = false
      it.canBeSelected = null
    }
    dispatch..GAME_CARD_SELECTION_CHANGED()
  }

  fun handOverIsValid() = cards.filter { it.isSelected }.size == 3

  fun grillIsValid() = when (selectedCards.size) {
    1    -> selectedCards.filter(isBlackCat).size == 1
    3    -> selectedCards.filter(isOfSuit(Suit.Hearts)).size == 3
    6    -> selectedCards.filter(isOfSuit(Suit.Hearts)).size == 6
    else -> false
  }

  fun canCardsBeSend() = when (allowedCardSelection) {
    AllowedCardSelection.HandOver -> handOverIsValid()
    AllowedCardSelection.Grill    -> grillIsValid()
    else                          -> false
  }

  fun order() {
    val cards = this.children.filterIsInstance(CardObject::class.java)
    cards.forEach { this -= it }

    val sortedCards = cards.sortedBy { it.customZIndex }
    sortedCards.forEach { this += it }
  }

  fun transformCard(card: CardObject, from: ICardsObject? = null, to: ICardsObject, duration: Float = 0f, valueChange: CardValueChange? = null) {
    from?.cards?.remove(card)
    from?.updateCards(duration)

    to.cards.add(card)
    to.updateCards(duration)
    to.updateCard(to.cards.lastIndex, duration, valueChange)
  }

  fun transformDeckCard(card: CardObject, from: ICardsObject? = null, to: ICardsObject, duration: Float = 0f, valueChange: CardValueChange? = null) {
    from?.cards?.remove(card)

    to.cards.add(card)
    to.updateCards(duration)
    to.updateCard(to.cards.lastIndex, duration, valueChange)
  }

  fun dealCards(playerHand: List<Card>) {
    val initialDelay = 1.5f
    val dealDuration = 0.15f
    val travelDuration = dealDuration * 4

    fun cardDelay(index: Int) = initialDelay + dealDuration * index

    val transformations: List<() -> Unit> = deck.cards.reversed().mapIndexed { index, card ->
      val to = when (index % 4) {
        0    -> bottomHand
        1    -> leftHand
        2    -> topHand
        3    -> rightHand
        else -> throw error("dealing cards")
      }
      val valueChange = when (index % 4) {
        0    -> CardValueChange(playerHand[index / 4].suit, playerHand[index / 4].rank)
        else -> null
      }

      fun() {
        transformDeckCard(
          card = card,
          from = deck,
          to = to,
          duration = travelDuration,
          valueChange = valueChange
        )
      }
    }

    repeat(deck.cards.size) { index ->
      this addAction delay(
        time = cardDelay(index),
        action = doAction {
          transformations[index]()
        }
      )
    }

    this addAction delay(
      time = cardDelay(deck.cards.size),
      action = doAction {
        send..DECK_DEALT()
      }
    )
  }

  fun passHandOver(handOver: Collection<Card>? = null, from: PlayerSide) {
    val cardsToPass = when (from) {
      PlayerSide.BOTTOM -> bottomHand.cards
        .filter { card ->
          handOver?.find { card.suit == it.suit && card.rank == it.rank } != null
        }
      PlayerSide.TOP    -> List(3) { topHand.cards.popRandom() }
      PlayerSide.LEFT   -> List(3) { leftHand.cards.popRandom() }
      PlayerSide.RIGHT  -> List(3) { rightHand.cards.popRandom() }
    }

    cardsToPass.forEach { card ->
      transformCard(
        card = card,
        from = when (from) {
          PlayerSide.BOTTOM -> bottomHand
          PlayerSide.TOP    -> topHand
          PlayerSide.LEFT   -> leftHand
          PlayerSide.RIGHT  -> rightHand
        },
        to = when (from) {
          PlayerSide.BOTTOM -> leftHandOver
          PlayerSide.TOP    -> rightHandOver
          PlayerSide.LEFT   -> topHandOver
          PlayerSide.RIGHT  -> bottomHandOver
        },
        valueChange = CardValueChange(null, null),
        duration = 1f
      )
    }
  }

  fun takeHandOver(handOver: List<Card>? = null, player: PlayerSide) {
    val cardsToTake = (when (player) {
      PlayerSide.BOTTOM -> bottomHandOver.cards
      PlayerSide.TOP    -> topHandOver.cards
      PlayerSide.LEFT   -> leftHandOver.cards
      PlayerSide.RIGHT  -> rightHandOver.cards
    }).clone()

    when (player) {
      PlayerSide.BOTTOM -> {
        if (handOver != null) {
          (cardsToTake zip handOver).forEach { (card, handOverCard) ->
            transformCard(
              card = card,
              from = bottomHandOver,
              to = bottomHand,
              valueChange = CardValueChange(handOverCard.suit, handOverCard.rank),
              duration = 1f
            )
          }
        }
      }
      else              -> {
        cardsToTake.forEach { card ->
          transformCard(
            card = card,
            from = when (player) {
              PlayerSide.TOP   -> topHandOver
              PlayerSide.LEFT  -> leftHandOver
              PlayerSide.RIGHT -> rightHandOver
              else             -> throw error("impossible")
            },
            to = when (player) {
              PlayerSide.TOP   -> topHand
              PlayerSide.LEFT  -> leftHand
              PlayerSide.RIGHT -> rightHand
              else             -> throw error("impossible")
            },
            valueChange = CardValueChange(null, null),
            duration = 1f
          )
        }
      }
    }
  }

  fun grillCards(grill: List<Card>, player: PlayerSide) {
    val cardsToGrill = when (player) {
      PlayerSide.BOTTOM -> bottomHand.cards.filter { it.isSelected }
      PlayerSide.TOP    -> List(grill.size) { topHand.cards.popRandom() }
      PlayerSide.LEFT   -> List(grill.size) { leftHand.cards.popRandom() }
      PlayerSide.RIGHT  -> List(grill.size) { rightHand.cards.popRandom() }
    }

    (cardsToGrill zip grill).forEach { (card, grillCard) ->
      transformCard(
        card = card,
        from = when (player) {
          PlayerSide.BOTTOM -> bottomHand
          PlayerSide.TOP    -> topHand
          PlayerSide.LEFT   -> leftHand
          PlayerSide.RIGHT  -> rightHand
        },
        to = when (player) {
          PlayerSide.BOTTOM -> bottomGrill
          PlayerSide.TOP    -> topGrill
          PlayerSide.LEFT   -> leftGrill
          PlayerSide.RIGHT  -> rightGrill
        },
        valueChange = when (player) {
          PlayerSide.BOTTOM -> null
          else              -> CardValueChange(grillCard.suit, grillCard.rank)
        },
        duration = 1f
      )
    }
  }

  fun playCard(card: Card, player: PlayerSide, fromGrill: Boolean = false) {

    val cardContainer = when (player) {
      PlayerSide.BOTTOM -> if (fromGrill) bottomGrill else bottomHand
      PlayerSide.TOP    -> if (fromGrill) topGrill else topHand
      PlayerSide.LEFT   -> if (fromGrill) leftGrill else leftHand
      PlayerSide.RIGHT  -> if (fromGrill) rightGrill else rightHand
    }

    val cardToPlay =
      when (player) {
        PlayerSide.BOTTOM ->
          cardContainer.cards
            .find { card.suit == it.suit && card.rank == it.rank }
        PlayerSide.TOP,
        PlayerSide.LEFT,
        PlayerSide.RIGHT  -> if (fromGrill) {
          cardContainer.cards
            .find { card.suit == it.suit && card.rank == it.rank }
        } else {
          cardContainer.cards.popRandom()
        }
      }

    if (cardToPlay != null) {
      transformCard(
        card = cardToPlay,
        from = cardContainer,
        to = table,
        valueChange = CardValueChange(card.suit, card.rank),
        duration = 1f
      )
    }
  }

  fun collectTrick() {
    val cards = table.cards.clone()

    cards.forEach { card ->
      transformCard(
        card = card,
        from = table,
        to = trick,
        duration = 1f
      )
    }
  }

  fun trickToPile(player: PlayerSide) {
    val cards = trick.cards.clone()

    cards.forEach { card ->
      transformCard(
        card = card,
        from = trick,
        to = when (player) {
          PlayerSide.BOTTOM -> bottomPile
          PlayerSide.TOP    -> topPile
          PlayerSide.LEFT   -> leftPile
          PlayerSide.RIGHT  -> rightPile
        },
        duration = 1f,
        valueChange = CardValueChange(null, null)
      )
    }
  }

  fun cardsToDeck(duration: Float = 1f) {
    cards.forEach { card ->
      transformCard(
        card = card,
        to = deck,
        valueChange = CardValueChange(null, null),
        duration = duration
      )
    }

    listOf(
      table,
      trick,
      bottomHand,
      topHand,
      leftHand,
      rightHand,
      bottomGrill,
      topGrill,
      leftGrill,
      rightGrill,
      bottomHandOver,
      topHandOver,
      leftHandOver,
      rightHandOver,
      bottomPile,
      topPile,
      leftPile,
      rightPile
    ).forEach { it.cards.clear() }
  }

  override fun dispose() {
    cards.forEach(CardObject::dispose)
    super.dispose()
  }
}