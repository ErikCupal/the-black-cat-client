package com.erikcupal.theblackcatclient.state

import com.erikcupal.theblackcatclient.helpers.log
import com.erikcupal.theblackcatclient.helpers.replace
import com.erikcupal.theblackcatclient.types.*

fun stateUpdaterFactory(state: State): (Any) -> Unit {
  return fun(action: Any) {
    // helper flag, changes to false if state was not changed
    var stateChanged = true

    when (action) {
      is REGISTERED               -> state.name = action.name
      is ROOM_CREATED             -> {
        state.room = action.name
        state.players = mutableListOf(state.name!!)
      }
      is ROOM_JOINED              -> {
        state.room = action.name
        state.players = action.players
      }
      is ROOM_LEFT                -> {
        state.room = null
        state.players = null
      }
      is PLAYER_JOINED            -> state.players?.add(action.player)
      is PLAYER_REPLACED_WITH_BOT -> state.players?.replace(action.player, action.bot)
      is DO_TAKE_HANDOVER         -> state.handOver = action.handOver
      else                        -> stateChanged = false
    }

    if (stateChanged) {
      log("🐉🐉🐉 STATE CHANGED 🐉🐉🐉", "action: $action, state: $state")
    }
  }
}