package com.erikcupal.theblackcatclient.assets

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.erikcupal.theblackcatclient.COLORS

/**
 * Scene2D styles
 */
class Styles {
  internal val input = TextFieldStyle()

  internal val button = TextButtonStyle()
  internal val smallNoBackgroundButton = TextButtonStyle()
  internal val mediumNoBackgroundButton = TextButtonStyle()

  internal val smallLabel = LabelStyle()
  internal val mediumLabel = LabelStyle()
  internal val largeLabel = LabelStyle()
  internal val errorLabel = LabelStyle()
  internal val chatText = LabelStyle()
  internal val chatPlayerLabel = LabelStyle()
  internal val scoresHeaderLabel = LabelStyle()

  fun load(assets: Assets) {

    // Input

    input.font = assets.get<BitmapFont>("inputFont.ttf")
    input.fontColor = COLORS.LIGHT_GREY
    input.background = assets.getDrawable("textfield")
    input.background.leftWidth = 20f
    input.background.rightWidth = 20f
    input.focusedBackground = assets.getDrawable("textfield_focused")
    input.focusedBackground.leftWidth = 20f
    input.focusedBackground.rightWidth = 20f
    input.cursor = assets.getDrawable("textfield_cursor")

    // Buttons

    button.up = assets.getDrawable("button_on")
    button.down = assets.getDrawable("button_off")
    button.font = assets.get("buttonFont.ttf")
    button.fontColor = COLORS.GREY_GOLD
    button.downFontColor = COLORS.DARK_GOLD
    button.disabledFontColor = COLORS.DARK_RED

    smallNoBackgroundButton.pressedOffsetY = -1f
    smallNoBackgroundButton.font = assets.get("smallNoBackgroundButtonFont.ttf")
    smallNoBackgroundButton.fontColor = COLORS.WHITE_GOLD
    smallNoBackgroundButton.downFontColor = COLORS.DARK_GOLD
    smallNoBackgroundButton.disabledFontColor = COLORS.MEDIUM_GREY

    mediumNoBackgroundButton.pressedOffsetY = -1f
    mediumNoBackgroundButton.font = assets.get("mediumNoBackgroundButtonFont.ttf")
    mediumNoBackgroundButton.fontColor = COLORS.WHITE_GOLD
    mediumNoBackgroundButton.downFontColor = COLORS.DARK_GOLD
    mediumNoBackgroundButton.disabledFontColor = COLORS.DARK_RED
    mediumNoBackgroundButton.overFontColor = COLORS.WHITE_ISH_GOLD

    // Labels

    smallLabel.font = assets.get<BitmapFont>("smallLabelFont.ttf")
    mediumLabel.font = assets.get<BitmapFont>("mediumLabelFont.ttf")
    largeLabel.font = assets.get<BitmapFont>("largeLabelFont.ttf")
    errorLabel.font = assets.get<BitmapFont>("errorLabelFont.ttf")
    chatText.font = assets.get<BitmapFont>("chatTextFont.ttf")
    chatPlayerLabel.font = assets.get<BitmapFont>("chatPlayerLabelFont.ttf")
    scoresHeaderLabel.font = assets.get<BitmapFont>("scoresHeaderFont.ttf")
  }
}