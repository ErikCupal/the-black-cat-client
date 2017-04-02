package com.erikcupal.theblackcatclient.gui

import com.erikcupal.theblackcatclient.core.GameCore
import com.badlogic.gdx.scenes.scene2d.Group
import rx.subscriptions.CompositeSubscription

/**
 * Parent class for Scene2D group classes
 *
 * Provides easier access to certain game properties
 * and auto-disposable RxKotlin subscription.
 */
open class GroupBase(game: GameCore): Group() {
  protected val assets = game.assets
  protected val messages = game.messages
  protected val state = game.state
  protected val style = assets.styles
  internal val dispatch = game.dispatch
  internal val send = game.send

  protected val subscription = CompositeSubscription()

  open fun dispose() {
    subscription.clear()
  }
}