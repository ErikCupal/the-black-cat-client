package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.Group
import com.erikcupal.theblackcatclient.GameCore
import rx.subscriptions.CompositeSubscription

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