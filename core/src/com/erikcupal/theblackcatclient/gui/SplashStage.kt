package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.types.GAME_SET_STAGE
import ktx.actors.alpha

/**
 * Stage with splash picture
 *
 * It is used to load assets
 * in background while visible.
 */
class SplashStage(game: GameCore) : StageBase(game) {

  private val backgroundTexture = Texture("textures/splash_screen.png")
  private val background = Image(toDrawable(backgroundTexture))

  var stageDidChanged = false

  init {
    invisibleCursor()

    background.setSize(WIDTH, HEIGHT)
    background.isVisible = false
    background.alpha = 0f
    this += background
    background.show(time = 1f)
  }

  override fun act(delta: Float) {

    if (shouldChangeToConnectStage()) {
      addAction(sequence(
        fadeOut(),
        doAction {
          loadCursor()
          dispatch..GAME_SET_STAGE("CONNECT_STAGE")
        }
      ))

      stageDidChanged = true
    }

    super.act(delta)
  }

  fun shouldChangeToConnectStage() = !stageDidChanged && assets.isLoaded()

  fun invisibleCursor() {
    val cursor = Pixmap(Gdx.files.internal("textures/invisible_cursor.png"))
    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0))
  }

  fun loadCursor() {
    val cursor = Pixmap(Gdx.files.internal("textures/cursor.png"))
    Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursor, 0, 0))
  }

  override fun dispose() {
    backgroundTexture.dispose()
    super.dispose()
  }
}