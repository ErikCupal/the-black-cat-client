package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.types.*
import ktx.scene2d.table
import rx.lang.kotlin.plusAssign

class ServersGroup(game: GameCore) : GroupBase(game) {

  val nameLabel = Label("Nick", style.smallLabel)
  val nickInput = TextField("", style.input)
  val serverLabel = Label("Server", style.smallLabel)
  val serverInput = TextField("http://localhost:3000", style.input)
  val connectButton = TextButton("Connect", style.button)
  val quitButton = TextButton("Quit game", style.button)

  val layout = table {
    add(nameLabel)
    row()
    add(nickInput).width(245f).height(65f).padBottom(20f)
    row()
    add(serverLabel)
    row()
    add(serverInput).width(245f).height(65f).padBottom(30f)
    row()
    add(connectButton).height(70f).padBottom(30f)
    row()
    add(quitButton).height(70f)
  }

  init {
    layout.setPosition(WIDTH / 2, HEIGHT / 2, Align.center)
    this += layout

    configureButtonEventHandlers(game)
    configureMessageHandlers()
  }

  private fun configureButtonEventHandlers(game: GameCore) {
    connectButton onTap {
      if (inputsNotEmpty() && !nickIsTooLong()) {
        when (game.isConnected()) {
          false -> dispatch..CONNECT(server = serverInput.text)
          true  -> send..REGISTER(name = nickInput.text)
        }
      } else if (nickIsTooLong()) {
        dispatch..GAME_NAME_TOO_LONG()
      }
    }

    quitButton onTap {
      parent addAction sequence(
        fadeOut(),
        doAction {
          Gdx.app.exit()
        }
      )
    }
  }

  private fun configureMessageHandlers() {
    subscription += messages.subscribe {
      when (it) {
        is CONNECTED -> send..REGISTER(name = nickInput.text)
      }
    }
  }

  private fun inputsNotEmpty() = serverInput.text.trim() != "" && nickInput.text.trim() != ""
  private fun nickIsTooLong() = nickInput.text.length > 100
}