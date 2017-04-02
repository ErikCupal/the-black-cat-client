package com.erikcupal.theblackcatclient.helpers

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Interpolation.fade
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.CENTER

/**
 * Scene2D helpers
 */

infix fun Actor.onTap(callback: (Actor) -> Unit) {
  val self = this
  this.addListener(object : ActorGestureListener() {
    override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
      val isDisabled = if (self is Button) { self.isDisabled } else false
      if (!isDisabled) {
        callback(self)
      }
    }
  })
}

infix fun Actor.onMouseEnter(callback: (Actor) -> Unit) {
  val self = this
  this.addListener(object : ClickListener() {
    override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
      if (pointer == -1) {
        callback(self)
      }
    }
  })
}

infix fun Actor.onMouseExit(callback: (Actor) -> Unit) {
  val self = this
  this.addListener(object : ClickListener() {
    override fun exit(event: InputEvent?, x: Float, y: Float, pointer: Int, toActor: Actor?) {
      if (pointer == -1) {
        callback(self)
      }
    }
  })
}

/**
 * Adds an action to the actor
 */
infix fun Actor.addAction(action: Action?) {
  if (action != null) {
    addAction(action)
  }
}

/**
 * Removes an action from the actor
 */
infix fun Actor.removeAction(action: Action?) {
  if (action != null) {
    removeAction(action)
  }
}

fun Actor.show(time: Float = 0.5f, interpolation: Interpolation = fade) {
  isVisible = true
  this addAction fadeIn(time, interpolation)
}

fun Actor.hide(time: Float = 0.5f, interpolation: Interpolation = fade) {
  this addAction sequence(
    fadeOut(time, interpolation),
    doAction { isVisible = false }
  )
}

fun Actor.center() = setPosition(CENTER.x, CENTER.y, Align.center)

fun doAction(callback: () -> Unit): Action = object : Action() {
  override fun act(delta: Float): Boolean {
    callback()
    return true
  }
}

/**
 * Adds an actor to the group
 */
operator fun Group.plusAssign(actor: Actor) {
  addActor(actor)
}

/**
 * Removes an actor from the group
 */
operator fun Group.minusAssign(actor: Actor) {
  removeActor(actor)
}

/**
 * Adds an actor to the stage
 */
operator fun Stage.plusAssign(actor: Actor) {
  addActor(actor)
}

/**
 * fixedLayout
 *
 * Layouts [[ScrollPane]] correctly.
 */
fun ScrollPane.fixedLayout() {
  layout()
  layout()
}

fun ScrollPane.scrollToBottom() {
  fixedLayout()
  scrollPercentY = 100f
}

// Actions

fun delay(time: Float, action: Action): DelayAction = Actions.delay(time, action)
fun delay(time: Float): DelayAction = Actions.delay(time)
fun sequence(vararg actions: Action): SequenceAction = Actions.sequence(*actions)
fun parallel(vararg actions: Action): ParallelAction = Actions.parallel(*actions)
fun fadeIn(time: Float = 1f, interpolation: Interpolation = fade): AlphaAction = Actions.fadeIn(time, interpolation)
fun fadeOut(time: Float = 1f, interpolation: Interpolation = fade): AlphaAction = Actions.fadeOut(time, interpolation)
fun scaleTo(x: Float, y: Float, duration: Float, interpolation: Interpolation = fade): ScaleToAction {
  return Actions.scaleTo(x, y, duration, interpolation)
}
fun moveTo(x: Float, y: Float, duration: Float, interpolation: Interpolation = fade): MoveToAction {
  return Actions.moveTo(x, y, duration, interpolation)
}
fun rotateTo(rotation: Float, duration: Float, interpolation: Interpolation = fade): RotateToAction {
  return Actions.rotateTo(rotation, duration, interpolation)
}

val fade: Interpolation = fade