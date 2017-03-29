package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.hide
import com.erikcupal.theblackcatclient.helpers.onTap
import com.erikcupal.theblackcatclient.helpers.plusAssign
import com.erikcupal.theblackcatclient.helpers.show
import com.erikcupal.theblackcatclient.types.*
import ktx.actors.alpha
import ktx.scene2d.table
import rx.lang.kotlin.plusAssign

class RoomsGroup(game: GameCore) : GroupBase(game) {

  val newRoomLabel = Label("New room", style.smallLabel)
  val roomInput = TextField("", style.input)
  val createRoomButton = TextButton("Create room", style.button)

  val leaveServerButton = TextButton("Leave server", style.button)

  val existingRoomsLabel = Label("Existing rooms", style.smallLabel)
  val roomButtons = Table()

  val layout = table {
    add(newRoomLabel)
    row()
    add(roomInput).height(65f).width(245f).padBottom(20f)
    row()
    add(createRoomButton).height(70f).padBottom(20f)
    row()
    add(leaveServerButton).height(70f).padBottom(30f)
    row()
    add(existingRoomsLabel).padBottom(10f)
    row()
    add(roomButtons)
  }

  init {

    existingRoomsLabel.isVisible = false
    existingRoomsLabel.alpha = 0f

    layout.setPosition(WIDTH / 2, HEIGHT - 300f)
    layout.align(Align.top)
    this += layout

    configureButtonEventHandlers()
    configureMessageHandlers()
  }

  private fun configureButtonEventHandlers() {
    createRoomButton onTap {
      if (roomInput.text.trim() != "" && !roomNameTooLong()) {
        send..CREATE_ROOM(name = roomInput.text)
      } else if (roomNameTooLong()) {
        dispatch..GAME_NAME_TOO_LONG()
      }
    }
    leaveServerButton onTap {
      dispatch..GAME_LEAVE_SERVER()
    }
  }

  private fun configureMessageHandlers() {
    subscription += messages.subscribe {
      when (it) {
        is AVAILABLE_ROOMS -> {
          updateExistingRoomsButtons(it.rooms)
          createRoomButton.isDisabled = it.rooms.size >= 4
        }
      }
    }
  }

  private fun updateExistingRoomsButtons(rooms: List<AvailableRoom>) {
    val roomsButtons = rooms.map { (name, available) ->
      val croppedName = if (name.length > 10) {
        name.substring(0..9) + "..."
      } else {
        name
      }

      val button = TextButton(croppedName, style.button)
      button.isDisabled = !available

      button
    }

    when (roomsButtons.size) {
      0    -> existingRoomsLabel.hide()
      else -> existingRoomsLabel.show()
    }

    removeExistingRoomsButtons()
    addExistingRoomsButtons(roomsButtons)
    configureRoomButtonsEventHandlers(roomsButtons)
  }

  private fun removeExistingRoomsButtons() {
    roomButtons.clearChildren()
  }

  private fun addExistingRoomsButtons(buttons: List<TextButton>) {
    buttons.forEach { button ->
      roomButtons.add(button).height(70f).padBottom(20f)
      roomButtons.row()
    }
  }

  private fun configureRoomButtonsEventHandlers(buttons: List<TextButton>) {
    buttons.forEach { button ->
      button onTap {
        send..JOIN_ROOM(name = button.text.toString())
      }
    }
  }

  private fun roomNameTooLong() = roomInput.text.length > 100
}