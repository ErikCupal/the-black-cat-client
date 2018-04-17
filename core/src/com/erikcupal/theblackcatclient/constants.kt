package com.erikcupal.theblackcatclient

import com.badlogic.gdx.math.Vector2
import com.erikcupal.theblackcatclient.helpers.rgba

val GAME_WIDTH = 1920f
val GAME_HEIGHT = 1080f

val WIDTH = GAME_WIDTH
val HEIGHT = GAME_HEIGHT

/**
 * Screen center
 */
val CENTER = Vector2(WIDTH / 2, HEIGHT / 2)
val GAME_SIZE = Vector2(WIDTH, HEIGHT)
/**
 * Table's offset from the bottom of screen
 */
val BOTTOM_TABLE_OFFSET = Vector2(0f, 30f)
val CARD_WIDTH = 499f
val CARD_HEIGHT = 696f
val CARD_INITIAL_SCALE = 0.15f

object COLORS {
  val WHITE = rgba(255, 255, 255, 1f)
  val TRANSLUCENT_WHITE = rgba(255, 255, 255, 0.8f)
  val DARK_RED = rgba(200, 10, 10, 1f)
  val BLOOD = rgba(100, 10, 10, 1f)
  val GREY_GOLD = rgba(215, 200, 130, 0.95f)
  val WHITE_GOLD = rgba(235, 220, 140, 1f)
  val WHITE_ISH_GOLD = rgba(195, 180, 100, 1f)
  val DARK_GOLD = rgba(160, 160, 90, 0.9f)
  val LIGHT_GREY = rgba(204, 204, 204, 0.7f)
  val MEDIUM_GREY = rgba(180, 180, 180, 0.8f)
  val LIGHT_BLACK = rgba(20, 20, 20, 1f)
  val TRANSLUCENT_BLACK = rgba(0, 0, 0, 0.8f)
}
