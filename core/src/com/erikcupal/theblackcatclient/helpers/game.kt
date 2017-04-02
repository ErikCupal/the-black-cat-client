package com.erikcupal.theblackcatclient.helpers

import com.erikcupal.theblackcatclient.types.Name
import com.erikcupal.theblackcatclient.types.PlayerSide
import com.erikcupal.theblackcatclient.types.State

fun getPlayerRotation(side: PlayerSide) = when (side) {
  PlayerSide.BOTTOM -> 0f
  PlayerSide.TOP    -> 180f
  PlayerSide.LEFT   -> -90f
  PlayerSide.RIGHT  -> 90f
}

fun State.getPlayerSide(player: Name): PlayerSide {
  val myIndex = players?.indexOf(name)
  val playerIndex = players?.indexOf(player)

  val indexDifference = if (myIndex != null && playerIndex != null) playerIndex - myIndex else null

  val normalizedDifference = if (indexDifference != null) {
    if (indexDifference <= 0) indexDifference + 4 else indexDifference
  } else null

  val playerSide = when (normalizedDifference) {
    1    -> PlayerSide.LEFT
    2    -> PlayerSide.TOP
    3    -> PlayerSide.RIGHT
    else -> PlayerSide.BOTTOM
  }

  return playerSide
}