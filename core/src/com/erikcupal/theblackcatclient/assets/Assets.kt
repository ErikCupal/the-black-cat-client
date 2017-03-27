package com.erikcupal.theblackcatclient.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.DEFAULT_CHARS
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.erikcupal.theblackcatclient.COLORS
import com.erikcupal.theblackcatclient.helpers.toDrawable

/**
 * Assets
 */
class Assets {

  private val manager = AssetManager()
  private val atlasPath = "textures/build/textures.atlas"
  val styles = Styles()
  private var stylesInitialized = false

  init {
    configureLoaders()
    loadAssets()
  }

  private fun configureLoaders() {
    val fileResolver = InternalFileHandleResolver()

    manager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(fileResolver))
    manager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(fileResolver))
    manager.setLoader(TextureAtlas::class.java, ".atlas", TextureAtlasLoader(fileResolver))
  }

  private fun loadAssets() {
    manager.load<TextureAtlas>(atlasPath, TextureAtlas::class.java)
    loadFonts()
  }

  private fun loadFonts() {
    // Input
    val inputFont = FreeTypeFontLoaderParameter()
    // Buttons
    val buttonFont = FreeTypeFontLoaderParameter()
    val smallNoBackgroundButtonFont = FreeTypeFontLoaderParameter()
    val mediumNoBackgroundButtonFont = FreeTypeFontLoaderParameter()
    // Labels
    val smallLabelFont = FreeTypeFontLoaderParameter()
    val mediumLabelFont = FreeTypeFontLoaderParameter()
    val largeLabelFont = FreeTypeFontLoaderParameter()
    val errorLabelFont = FreeTypeFontLoaderParameter()
    val chatTextFont = FreeTypeFontLoaderParameter()
    val chatPlayerLabelFont = FreeTypeFontLoaderParameter()
    val scoresHeaderFont = FreeTypeFontLoaderParameter()

    listOf(
      inputFont,

      buttonFont,
      smallNoBackgroundButtonFont,
      mediumNoBackgroundButtonFont,

      smallLabelFont,
      largeLabelFont,
      mediumLabelFont,
      errorLabelFont,
      chatTextFont,
      chatPlayerLabelFont,
      scoresHeaderFont
    ).forEach {
      it.fontFileName = "fonts/merriweather_regular.ttf"
      it.fontParameters.genMipMaps = true
      it.fontParameters.minFilter = TextureFilter.MipMapLinearLinear
      it.fontParameters.magFilter = TextureFilter.MipMapLinearLinear
      it.fontParameters.kerning = true
      it.fontParameters.hinting = FreeTypeFontGenerator.Hinting.AutoMedium
      it.fontParameters.characters = DEFAULT_CHARS + "áéěíýóúůščřžťďňÁÉĚÍÝÓÚŮŠČŘŽŤĎŇ"
    }

    // Input

    inputFont.fontParameters.size = 24

    // Buttons

    buttonFont.fontParameters.size = 26
    buttonFont.fontParameters.borderColor = COLORS.LIGHT_BLACK
    buttonFont.fontParameters.borderWidth = 2f

    smallNoBackgroundButtonFont.fontParameters.size = 24
    smallNoBackgroundButtonFont.fontParameters.borderColor = COLORS.TRANSLUCENT_BLACK
    smallNoBackgroundButtonFont.fontParameters.borderWidth = 3f

    mediumNoBackgroundButtonFont.fontParameters.size = 32
    mediumNoBackgroundButtonFont.fontParameters.color = COLORS.TRANSLUCENT_WHITE
    mediumNoBackgroundButtonFont.fontParameters.borderColor = COLORS.TRANSLUCENT_BLACK
    mediumNoBackgroundButtonFont.fontParameters.borderWidth = 3f

    // Labels

    smallLabelFont.fontParameters.size = 28
    smallLabelFont.fontParameters.color = COLORS.WHITE_ISH_GOLD
    smallLabelFont.fontParameters.borderColor = COLORS.TRANSLUCENT_BLACK
    smallLabelFont.fontParameters.borderWidth = 2f

    mediumLabelFont.fontParameters.size = 40
    mediumLabelFont.fontParameters.color = COLORS.WHITE_GOLD
    mediumLabelFont.fontParameters.borderColor = COLORS.TRANSLUCENT_BLACK
    mediumLabelFont.fontParameters.borderWidth = 3f

    largeLabelFont.fontParameters.size = 60
    largeLabelFont.fontParameters.color = COLORS.WHITE
    largeLabelFont.fontParameters.borderColor = COLORS.TRANSLUCENT_BLACK
    largeLabelFont.fontParameters.borderWidth = 5f

    errorLabelFont.fontParameters.size = 30
    errorLabelFont.fontParameters.color = COLORS.WHITE
    errorLabelFont.fontParameters.borderColor = COLORS.TRANSLUCENT_BLACK
    errorLabelFont.fontParameters.borderWidth = 3f

    chatTextFont.fontParameters.size = 25
    chatTextFont.fontParameters.color = COLORS.TRANSLUCENT_BLACK

    chatPlayerLabelFont.fontParameters.size = 28
    chatPlayerLabelFont.fontParameters.color = COLORS.BLOOD

    scoresHeaderFont.fontParameters.size = 28
    scoresHeaderFont.fontParameters.color = COLORS.TRANSLUCENT_BLACK

    // Load fonts

    manager.load("inputFont.ttf", BitmapFont::class.java, inputFont)

    manager.load("buttonFont.ttf", BitmapFont::class.java, buttonFont)
    manager.load("smallNoBackgroundButtonFont.ttf", BitmapFont::class.java, smallNoBackgroundButtonFont)
    manager.load("mediumNoBackgroundButtonFont.ttf", BitmapFont::class.java, mediumNoBackgroundButtonFont)

    manager.load("smallLabelFont.ttf", BitmapFont::class.java, smallLabelFont)
    manager.load("mediumLabelFont.ttf", BitmapFont::class.java, mediumLabelFont)
    manager.load("largeLabelFont.ttf", BitmapFont::class.java, largeLabelFont)
    manager.load("errorLabelFont.ttf", BitmapFont::class.java, errorLabelFont)
    manager.load("chatTextFont.ttf", BitmapFont::class.java, chatTextFont)
    manager.load("chatPlayerLabelFont.ttf", BitmapFont::class.java, chatPlayerLabelFont)
    manager.load("scoresHeaderFont.ttf", BitmapFont::class.java, scoresHeaderFont)
  }

  fun isLoaded(): Boolean {
    if (manager.update()) {
      if (!stylesInitialized) {
        initializeStyles()
      }
      return true
    } else {
      return false
    }
  }

  private fun initializeStyles() {
    styles.load(this)
  }

  fun dispose() {
    manager.dispose()
  }

  fun <T> get(fileName: String): T = manager.get<T>(fileName)

  fun getRegion(fileName: String): AtlasRegion {
    return manager.get<TextureAtlas>(atlasPath, TextureAtlas::class.java).findRegion(fileName)
  }

  fun getDrawable(fileName: String): Drawable {
    return toDrawable(manager.get<TextureAtlas>(atlasPath, TextureAtlas::class.java).findRegion(fileName))
  }
}