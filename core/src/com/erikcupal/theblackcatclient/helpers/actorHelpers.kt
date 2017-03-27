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

operator fun Stage.plusAssign(actor: Actor) {
  addActor(actor)
}

infix fun Actor.addAction(action: Action?) {
  if (action != null) {
    addAction(action)
  }
}

operator fun Actor.minusAssign(listener: EventListener) {
  removeListener(listener)
}

infix fun Actor.removeAction(action: Action?) {
  if (action != null) {
    removeAction(action)
  }
}

fun Actor.center() = setPosition(CENTER.x, CENTER.y, Align.center)

fun scaleToAction(scale: Float, duration: Float, interpolation: Interpolation): ScaleToAction {
  val action = ScaleToAction()
  action.setScale(scale)
  action.duration = duration
  action.interpolation = interpolation
  return action
}

fun scaleToAction(scaleX: Float, scaleY: Float, duration: Float, interpolation: Interpolation): ScaleToAction {
  val action = ScaleToAction()
  action.setScale(scaleX, scaleY)
  action.duration = duration
  action.interpolation = interpolation
  return action
}

fun moveToAction(x: Float, y: Float, duration: Float, interpolation: Interpolation): MoveToAction {
  val action = MoveToAction()
  action.setPosition(x, y)
  action.duration = duration
  action.interpolation = interpolation
  return action
}

fun rotateToAction(rotation: Float, duration: Float, interpolation: Interpolation): RotateToAction {
  val action = RotateToAction()
  action.rotation = rotation
  action.duration = duration
  action.interpolation = interpolation
  return action
}

inline fun optimizedDoAction(crossinline callback: () -> Unit): Action = object : Action() {
  override fun act(delta: Float): Boolean {
    callback()
    return true
  }
}

fun doAction(callback: () -> Unit): Action = object : Action() {
  override fun act(delta: Float): Boolean {
    callback()
    return true
  }
}

operator fun Group.plusAssign(actor: Actor) {
  addActor(actor)
}

operator fun Group.minusAssign(actor: Actor) {
  removeActor(actor)
}

/**
 * Necessary to fix bug in Scene2D
 */
fun ScrollPane.fixedLayout() {
  layout()
  layout()
}

fun ScrollPane.scrollToBottom() {
  fixedLayout()
  scrollPercentY = 100f
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

val fade: Interpolation = fade

fun delay(time: Float, action: Action): DelayAction = Actions.delay(time, action)
fun delay(time: Float): DelayAction = Actions.delay(time)
fun sequence(vararg actions: Action): SequenceAction = Actions.sequence(*actions)
fun parallel(vararg actions: Action): ParallelAction = Actions.parallel(*actions)
fun forever(action: Action): RepeatAction = Actions.forever(action)
fun fadeIn(time: Float = 1f, interpolation: Interpolation = fade): AlphaAction = Actions.fadeIn(time, interpolation)
fun fadeOut(time: Float = 1f, interpolation: Interpolation = fade): AlphaAction = Actions.fadeOut(time, interpolation)