package com.erikcupal.theblackcatclient.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

fun clearScreen() {
  Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
}

fun toDrawable(texture: Texture): Drawable = TextureRegionDrawable(TextureRegion(texture))

fun toDrawable(texture: TextureRegion): Drawable = TextureRegionDrawable(texture)

fun rgba(
  red: Int,
  green: Int,
  blue: Int,
  alpha: Float
): Color {
  return Color(red / 255f, green / 255f, blue / 255f, alpha)
}

operator fun Vector2.plus(v: Vector2): Vector2 {
  return Vector2(
    x + v.x,
    y + v.y
  )
}

operator fun Vector2.minus(v: Vector2): Vector2 {
  return Vector2(
    x - v.x,
    y - v.y
  )
}