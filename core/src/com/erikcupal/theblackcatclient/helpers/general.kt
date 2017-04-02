package com.erikcupal.theblackcatclient.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.erikcupal.theblackcatclient.types.Name
import com.erikcupal.theblackcatclient.types.PlayerSide
import com.erikcupal.theblackcatclient.types.State

fun log(tag: String, message: String = "") {
  Gdx.app.log(tag, message)
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

fun <E> Collection<E>.clone() = map { it }

