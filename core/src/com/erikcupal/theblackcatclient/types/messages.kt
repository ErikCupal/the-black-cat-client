package com.erikcupal.theblackcatclient.types

import com.erikcupal.theblackcatclient.gameObjects.CardObject

/**
 * Client socket Messages
 */

data class CONNECT(val server: Address)

data class DISCONNECT(val EMPTY: Boolean = true)

data class REGISTER(val name: Name)
data class CHANGE_NAME(val name: Name)
data class CREATE_ROOM(val name: Name)
data class JOIN_ROOM(val name: Name)
data class LEAVE_ROOM(val EMPTY: Boolean = true)
data class ADD_BOT(val EMPTY: Boolean = true)

data class PLAY_HAND_OVER(val handOver: HandOver)
data class TAKE_HANDOVER(val EMPTY: Boolean = true)
data class PLAY_GRILL(val grill: Grill)
data class PLAY_CARD(val card: Card)
data class DECK_DEALT(val EMPTY: Boolean = true)
data class I_AM_READY(val EMPTY: Boolean = true)
data class I_WANT_NEW_GAME(val EMPTY: Boolean = true)

data class GET_ROOMS(val EMPTY: Boolean = true)
data class SEND_CHAT_MESSAGE(val text: String)

/**
 * Server socket messages
 */

data class CONNECTED(val EMPTY: Boolean = true)

data class CONNECTING(val EMPTY: Boolean = true)
data class DISCONNECTED(val EMPTY: Boolean = true)

data class CONNECTION_CONFIRMATION(val EMPTY: Boolean = true)
data class CONNECT_FAILED(val EMPTY: Boolean = true)

data class REGISTERED(val name: Name)
data class NAME_TAKEN(val EMPTY: Boolean = true)

data class ROOM_CREATED(val name: Name)
data class ROOM_NAME_TAKEN(val EMPTY: Boolean = true)
data class ROOM_JOINED(val name: Name, val players: MutableList<Name>)
data class ROOM_FULL(val EMPTY: Boolean = true)
data class ROOM_LEFT(val EMPTY: Boolean = true)
data class PLAYER_JOINED(val player: Name)
data class PLAYER_REPLACED_WITH_BOT(val player: Name, val bot: Name)

data class GAME_STARTED(val EMPTY: Boolean = true)
data class GAME_ENDED(val EMPTY: Boolean = true)

data class NEXT_ROUND(val EMPTY: Boolean = true)
data class NEXT_TURN(val EMPTY: Boolean = true, val playerOnTurn: Name)

data class DEAL_DECK(val hand: BottomHand)
data class DO_PASS_HANDOVER(val EMPTY: Boolean = true)
data class DO_TAKE_HANDOVER(val handOver: HandOver)

data class HAND_OVER_PLAYED(val from: Name)
data class HAND_OVER_TAKEN(val player: Name)
data class GRILL_PLAYED(val player: Name, val grill: Grill)
data class CARD_PLAYED(val player: Name, val card: Card, val cardFromGrill: Boolean)
data class TRICK_COLLECTED(val EMPTY: Boolean = true)
data class TRICK_FINISHED(val receiver: Name)

data class AVAILABLE_ROOMS(val roomNames: MutableList<Name>)
data class UPDATED_SCORES(val scores: MutableList<LastPlayerScore>)
data class CHAT_MESSAGE(val text: String, val player: Name)

/**
 * Game
 */

data class GAME_LEAVE_SERVER(val EMPTY: Boolean = true)

data class GAME_SET_STAGE(val stageName: Name)
data class GAME_CARD_SELECTION_CHANGED(val EMPTY: Boolean = true)
data class GAME_HANDOVER_SELECTION_CHANGED(val readyToSend: Boolean)
data class GAME_GRILL_SELECTION_CHANGED(val readyToSend: Boolean)
data class GAME_PLAY_CARD(val card: CardObject)
data class GAME_NAME_TOO_LONG(val EMPTY: Boolean = true)

/**
 * Buttons
 */

data class READY_BUTTON(val EMPTY: Boolean = true)

data class TAKE_ALL_BUTTON(val EMPTY: Boolean = true)
data class TAKE_NOTHING_BUTTON(val EMPTY: Boolean = true)
data class PASS_CARDS_BUTTON(val EMPTY: Boolean = true)
data class TAKE_CARDS_BUTTON(val EMPTY: Boolean = true)
data class SORT_HAND_BUTTON(val EMPTY: Boolean = true)
data class GRILL_CARDS_BUTTON(val EMPTY: Boolean = true)
