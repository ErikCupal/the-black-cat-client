package com.erikcupal.theblackcatclient.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.erikcupal.theblackcatclient.types.Name
import com.erikcupal.theblackcatclient.types.PlayerSide
import com.erikcupal.theblackcatclient.types.State


fun log(tag: String, message: String = "") {
  Gdx.app.log(tag, message)
}

fun playerRotation(side: PlayerSide) = when (side) {
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

fun <E> MutableList<E>.popRandom(): E {
  val number = MathUtils.random(size - 1)
  val item = get(number)
  removeAt(number)
  return item
}

fun <E> MutableList<E>.replace(itemToReplace: E, newItem: E) {
  val index = indexOf(itemToReplace)
  if (index != -1) {
    add(index, newItem)
    remove(itemToReplace)
  }
}

fun <E> Collection<E>.copy() = map { it }

