package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.WIDTH
import com.erikcupal.theblackcatclient.helpers.plusAssign

class PlayerNameLabel(
  text: String = "",
  style: Label.LabelStyle? = null
) : Group() {

  val label = Label(text, style)
  var text
    get() = label.text.toString()
    set(value) {
      label.setText(value)
    }

  init {
    this.text = text
    label.setEllipsis(true)
    label.width = WIDTH / 3f
    label.setPosition(0f, 0f, Align.center)
    label.setAlignment(Align.center)
    this += label
  }
}