package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.types.*
import ktx.actors.alpha
import rx.lang.kotlin.plusAssign

class GameButtonsGroup(game: GameCore) : GroupBase(game) {

  val readyButton = createButton("I AM READY", style.mediumNoBackgroundButton)
  val passCardsButton = createButton("PASS 3 CARDS", style.mediumNoBackgroundButton)
  val takeCardsButton = createButton("TAKE CARDS", style.smallNoBackgroundButton)
  val sortHandButton = createButton("SORT HAND", style.smallNoBackgroundButton)
  val grillCardsButton = createButton("GRILL CARDS", style.mediumNoBackgroundButton)

  init {
    readyButton.setPosition(500f, 240f)
    readyButton.rotation = -10f

    passCardsButton.setPosition(440f, 190f)
    passCardsButton.rotation = -10f
    passCardsButton.isDisabled = true

    grillCardsButton.setPosition(1230f, 150f)
    grillCardsButton.rotation = 10f
    grillCardsButton.isDisabled = true

    takeCardsButton.setPosition(1100f, 65f)

    sortHandButton.setPosition(CENTER.x, 65f, Align.bottom)
    sortHandButton.alpha = 0.3f
    sortHandButton.isVisible = true

    configureButtonEventHandlers()
    configureMessageHandlers()
  }

  private fun configureButtonEventHandlers() {
    sortHandButton onMouseEnter {
      sortHandButton addAction Actions.alpha(1f, 0.15f, fade)
    }
    sortHandButton onMouseExit {
      sortHandButton addAction Actions.alpha(0.3f, 0.15f, fade)
    }

    readyButton onTap { dispatch..READY_BUTTON() }
    passCardsButton onTap { dispatch..PASS_CARDS_BUTTON() }
    grillCardsButton onTap { dispatch..GRILL_CARDS_BUTTON() }
    takeCardsButton onTap { dispatch..TAKE_CARDS_BUTTON() }
    sortHandButton onTap { dispatch..SORT_HAND_BUTTON() }
  }

  private fun configureMessageHandlers() {
    subscription += messages.subscribe {
      when (it) {
        is DO_PASS_HANDOVER                -> passCardsButton.show()
        is PASS_CARDS_BUTTON               -> passCardsButton.hide()
        is DO_TAKE_HANDOVER                -> takeCardsButton.show()
        is TAKE_CARDS_BUTTON               -> {
          takeCardsButton.hide()
          grillCardsButton.show()
          readyButton.show()
        }
        is GAME_HANDOVER_SELECTION_CHANGED -> passCardsButton.isDisabled = !it.readyToSend
        is GAME_GRILL_SELECTION_CHANGED    -> grillCardsButton.isDisabled = !it.readyToSend
        is READY_BUTTON                    -> {
          grillCardsButton.hide()
          readyButton.hide()
        }
      }
    }
  }

  fun createButton(text: String, style: TextButtonStyle): TextButton {
    val button = TextButton(text, style)
    button.isTransform = true
    button.alpha = 0f
    button.isVisible = false
    this += button
    return button
  }
}
