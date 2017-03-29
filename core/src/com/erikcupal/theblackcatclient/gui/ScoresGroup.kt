package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.plusAssign
import com.erikcupal.theblackcatclient.helpers.scrollToBottom
import com.erikcupal.theblackcatclient.types.*
import rx.lang.kotlin.plusAssign

class ScoresGroup(game: GameCore) : GroupBase(game) {

  val scoresLabel = Label("Scores", style.mediumLabel)
  val scoresBackground = Image(assets.getDrawable("scores"))

  val header = ScoresHeaderGroup(game)
  val footer = ScoresFooterGroup(game)

  val scoresFeed = Table()
  val scrollPane = ScrollPane(scoresFeed)

  val playersPointsSums = MutableList(4) { 0 }

  init {
    this += scoresLabel
    this += scoresBackground
    this += header
    this += footer
    this += scrollPane

    scoresLabel.setPosition(730f, HEIGHT - 170f, Align.bottom)

    scoresBackground.width = 1300f
    scoresBackground.height = 790f
    scoresBackground.setPosition(100f, 115f, Align.bottomLeft)

    scoresFeed.pad(20f)
    scoresFeed.align(Align.topLeft)

    scrollPane.setScrollingDisabled(true, false)
    scrollPane.width = 1300f
    scrollPane.height = 585f - 20f
    scrollPane.setPosition(100f, 230f + 20f, Align.bottomLeft)

    configureMessageHandlers()
  }

  private fun configureMessageHandlers() {
    subscription += messages.subscribe {
      when (it) {
        is UPDATED_SCORES -> addScores(it.scores)
      }
    }
  }

  private fun getPlayerScore(player: Name, scores: List<LastPlayerScore>): Int {
    val points = scores.find { it.player == player }?.points
    return points ?: 0
  }

  private fun createGameNumberLabel(): Label {
    val currentGame = scoresFeed.rows + 1
    return Label("#$currentGame", style.scoresHeaderLabel)
  }

  private fun createPlayerScoreLabel(playerLabel: Label, scores: List<LastPlayerScore>): Label {
    val points = getPlayerScore(playerLabel.text.toString(), scores).toString()
    return Label(points, style.scoresHeaderLabel)
  }

  private fun addScores(scores: List<LastPlayerScore>) {

    val gameNumberLabel = createGameNumberLabel()
    val playerScoreLabel1 = createPlayerScoreLabel(header.playerLabel1, scores)
    val playerScoreLabel2 = createPlayerScoreLabel(header.playerLabel2, scores)
    val playerScoreLabel3 = createPlayerScoreLabel(header.playerLabel3, scores)
    val playerScoreLabel4 = createPlayerScoreLabel(header.playerLabel4, scores)

    val points = scores.map { it.points }

    playersPointsSums.forEachIndexed { index, _ ->
      // Add points to sums list
      playersPointsSums[index] += points[index]
      // Set points to label
      val playerPoints = points[index].toString()
      when (index) {
        0 -> playerScoreLabel1.setText(playerPoints)
        1 -> playerScoreLabel2.setText(playerPoints)
        2 -> playerScoreLabel3.setText(playerPoints)
        3 -> playerScoreLabel4.setText(playerPoints)
      }
    }

    // Add labels to scoresFeed
    listOf(
      gameNumberLabel,
      playerScoreLabel1,
      playerScoreLabel2,
      playerScoreLabel3,
      playerScoreLabel4
    ).forEachIndexed { index, label ->
      label.setAlignment(Align.center)

      val isGameNumber = index == 0
      when (isGameNumber) {
        true  -> scoresFeed.add(label).width(170f)
        false -> scoresFeed.add(label).width(240f).pad(15f)
      }
    }

    scoresFeed.row()
    scrollPane.scrollToBottom()

    // Update final points and order
    footer.updateFinalPointsAndOrder(playersPointsSums)
  }

  override fun dispose() {
    header.dispose()
    super.dispose()
  }
}