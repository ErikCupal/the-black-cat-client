package com.erikcupal.theblackcatclient.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.erikcupal.theblackcatclient.core.GameCore

/**
 * Desktop game launcher
 *
 * Starts in windowed mode by default.
 */
object DesktopLauncher {
  @JvmStatic
  fun main(arg: Array<String>) {

    val config = LwjglApplicationConfiguration()
    val displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode()

    config.setFromDisplayMode(displayMode)
    config.title = "The Black Cat"
    config.fullscreen = false

    addIcon("icon/icon-16.png", config)
    addIcon("icon/icon-32.png", config)
    addIcon("icon/icon-64.png", config)
    addIcon("icon/icon-128.png", config)
    addIcon("icon/icon-256.png", config)

    LwjglApplication(GameCore(), config)
  }

  private fun addIcon(iconPath: String, config: LwjglApplicationConfiguration) {
    config.addIcon(iconPath, Files.FileType.Internal)
  }
}
