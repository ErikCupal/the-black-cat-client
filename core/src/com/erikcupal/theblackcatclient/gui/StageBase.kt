package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.helpers.fadeIn
import com.erikcupal.theblackcatclient.helpers.fadeOut
import rx.subscriptions.CompositeSubscription

/**
 * Parent class for Scene2D stage classes
 *
 * Provides easier access to certain game properties
 * and auto-disposable RxKotlin subscription.
 */
open class StageBase(game: GameCore): Stage(FitViewport(WIDTH, HEIGHT), game.batch) {

  protected val state = game.state
  protected val assets = game.assets
  protected val style = game.assets.styles
  protected val messages = game.messages
  internal val dispatch = game.dispatch
  internal val send = game.send

  protected val subscription = CompositeSubscription()

  override fun dispose() {
    subscription.clear()
    super.dispose()
  }

  open fun show() {
    addAction(fadeIn())
  }

  open fun hide() {
    addAction(fadeOut())
  }
}