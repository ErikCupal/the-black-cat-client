package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.onTap
import com.erikcupal.theblackcatclient.helpers.plusAssign
import com.erikcupal.theblackcatclient.helpers.scrollToBottom
import com.erikcupal.theblackcatclient.helpers.show
import com.erikcupal.theblackcatclient.types.*

class ChatGroup(game: GameCore) : GroupBase(game) {

  val label = Label("Chat", style.mediumLabel)
  val messagesFeed = Table()
  val scrollPaneBackground = Image(assets.getDrawable("chat"))
  val scrollPane = ScrollPane(messagesFeed)
  val messageInput = TextField("", style.input)
  val sendButton = TextButton("Send", style.button)

  init {
    this += label
    this += scrollPaneBackground
    this += scrollPane
    this += messageInput
    this += sendButton

    val BOX_WIDTH = 400f
    val BOX_HEIGHT = 700f
    val BOX_RIGHT_OFFSET = 100f
    val BOX_BOTTOM_OFFSET = 200f

    label.setPosition(WIDTH - BOX_RIGHT_OFFSET - BOX_WIDTH / 2, HEIGHT - 170f, Align.bottom)

    scrollPaneBackground.width = BOX_WIDTH
    scrollPaneBackground.height = BOX_HEIGHT
    scrollPaneBackground.setPosition(WIDTH - BOX_RIGHT_OFFSET, BOX_BOTTOM_OFFSET, Align.bottomRight)

    scrollPane.setScrollingDisabled(true, false)
    scrollPane.width = BOX_WIDTH
    scrollPane.height = BOX_HEIGHT - 20f
    scrollPane.setPosition(WIDTH - BOX_RIGHT_OFFSET, BOX_BOTTOM_OFFSET + 10f, Align.bottomRight)

    messagesFeed.pad(20f)
    messagesFeed.align(Align.bottomLeft)

    messageInput.width = BOX_WIDTH
    messageInput.height = 70f
    messageInput.setPosition(WIDTH - BOX_RIGHT_OFFSET, 120f, Align.bottomRight)
    messageInput.style.background.leftWidth = 20f
    messageInput.style.background.rightWidth = 20f

    sendButton.width = 300f
    sendButton.height = 70f
    sendButton.setPosition(WIDTH - BOX_RIGHT_OFFSET - BOX_WIDTH / 2, 20f, Align.bottom)
    sendButton.padLeft(15f)
    sendButton.padRight(15f)
    sendButton.show()

    configureMessageHandlers()
    configureButtonEventHandlers()
  }

  private fun configureMessageHandlers() {
    messages.subscribe {
      when (it) {
        is CHAT_MESSAGE -> addMessage(it.player, it.text)
      }
    }
  }

  private fun configureButtonEventHandlers() {
    sendButton onTap {
      if (messageInput.text.trim() != "") {
        send..SEND_CHAT_MESSAGE(text = messageInput.text)
        messageInput.text = ""
      }
    }
  }

  private fun addMessage(player: String, text: String) {

    val playerLabel = Label("$player:", style.chatPlayerLabel)
    playerLabel.setEllipsis(true)

    val textLabel = Label(text, style.chatText)
    textLabel.setWrap(true)

    messagesFeed.add(playerLabel).width(360f).align(Align.left).padBottom(10f)
    messagesFeed.row()
    messagesFeed.add(textLabel).width(360f).align(Align.left).padBottom(30f)
    messagesFeed.row()

    scrollPane.scrollToBottom()
  }
}