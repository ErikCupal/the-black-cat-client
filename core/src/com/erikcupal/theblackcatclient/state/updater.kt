package com.erikcupal.theblackcatclient.state

import com.erikcupal.theblackcatclient.helpers.log
import com.erikcupal.theblackcatclient.helpers.replace
import com.erikcupal.theblackcatclient.types.*

/**
 * stateUpdaterFactory
 *
 * Creates mutative updateState function, that accepts message.
 * If the message matches, the game state updates.
 */
fun stateUpdaterFactory(state: State): (Any) -> Unit {
  val updateState = fun(message: Any) {
    // helper flag, changes to false if state was not changed
    var stateChanged = true

    when (message) {
      is REGISTERED               -> state.name = message.name
      is ROOM_CREATED             -> {
        state.room = message.name
        state.players = mutableListOf(state.name!!)
      }
      is ROOM_JOINED              -> {
        state.room = message.name
        state.players = message.players
      }
      is ROOM_LEFT                -> {
        state.room = null
        state.players = null
      }
      is PLAYER_JOINED            -> state.players?.add(message.player)
      is PLAYER_REPLACED_WITH_BOT -> state.players?.replace(message.player, message.bot)
      is DO_TAKE_HANDOVER         -> state.handOver = message.handOver
      else                        -> stateChanged = false
    }

    if (stateChanged) {
      log("🐉🐉🐉 STATE CHANGED 🐉🐉🐉", "message: $message, state: $state")
    }
  }

  return updateState
}