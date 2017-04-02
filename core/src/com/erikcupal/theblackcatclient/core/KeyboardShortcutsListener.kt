package com.erikcupal.theblackcatclient.core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter

/**
 * KeyboardShortcutsListener
 *
 * Changes to fullscreen mode when F11 pressed.
 */
class KeyboardShortcutsListener : InputAdapter() {
  val F11 = 254

  override fun keyDown(keycode: Int): Boolean {
    return when (keycode) {
      F11  -> {
        when (Gdx.graphics.isFullscreen) {
          true  -> Gdx.graphics.setWindowedMode(Gdx.graphics.width, Gdx.graphics.height)
          false -> Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        }
        true
      }
      else -> super.keyDown(keycode)
    }
  }

}