package com.erikcupal.theblackcatclient.desktop

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.tools.texturepacker.TexturePacker

fun packTextures() {
  val settings = TexturePacker.Settings()
  settings.maxWidth = 2048
  settings.maxHeight = 2048
  settings.filterMin = Texture.TextureFilter.MipMapLinearLinear
  settings.filterMag = Texture.TextureFilter.MipMapLinearLinear
  TexturePacker.process(settings, "textures", "textures/build", "textures")
}