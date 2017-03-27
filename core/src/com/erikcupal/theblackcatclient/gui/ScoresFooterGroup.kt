package com.erikcupal.theblackcatclient.gui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.erikcupal.theblackcatclient.GameCore
import com.erikcupal.theblackcatclient.helpers.plusAssign

class ScoresFooterGroup(game: GameCore) : GroupBase(game) {

  val footer = Table()

  val allLabel = Label("ALL", style.scoresHeaderLabel)

  val playerOrderLabel1 = Label("", style.scoresHeaderLabel)
  val playerOrderLabel2 = Label("", style.scoresHeaderLabel)
  val playerOrderLabel3 = Label("", style.scoresHeaderLabel)
  val playerOrderLabel4 = Label("", style.scoresHeaderLabel)

  val playerPointsLabel1 = Label("", style.scoresHeaderLabel)
  val playerPointsLabel2 = Label("", style.scoresHeaderLabel)
  val playerPointsLabel3 = Label("", style.scoresHeaderLabel)
  val playerPointsLabel4 = Label("", style.scoresHeaderLabel)

  init {
    this += footer
    footer.width = 1300f
    footer.setPosition(100f, 190f, Align.bottomLeft)

    val finalOrderAndScoresTable = Table()

    listOf(
      allLabel,
      playerOrderLabel1,
      playerOrderLabel2,
      playerOrderLabel3,
      playerOrderLabel4,
      playerPointsLabel1,
      playerPointsLabel2,
      playerPointsLabel3,
      playerPointsLabel4
    ).forEachIndexed { index, label ->

      label.setAlignment(Align.center)

      val isAllLabel = index == 0
      when (isAllLabel) {
        true  -> footer.add(allLabel).width(110f).padRight(60f)
        false -> finalOrderAndScoresTable.add(label).width(240f).padLeft(15f).padRight(15f)
      }

      val shouldDoRow = index == 4
      if (shouldDoRow) {
        finalOrderAndScoresTable.row()
      }
    }

    footer.add(finalOrderAndScoresTable).width(1020f)
  }

  fun updateFinalPointsAndOrder(playerPointsSums: List<Int>) {
    val playersWithPoints =
      playerPointsSums zip listOf(
        Pair(playerOrderLabel1, playerPointsLabel1),
        Pair(playerOrderLabel2, playerPointsLabel2),
        Pair(playerOrderLabel3, playerPointsLabel3),
        Pair(playerOrderLabel4, playerPointsLabel4)
      )

    playersWithPoints
      .sortedBy { (points) -> points }
      .forEachIndexed { index, (points, playerLabels) ->
        val (orderLabel, pointsLabel) = playerLabels
        val order = index + 1
        orderLabel.setText("#$order")
        pointsLabel.setText(points.toString())
      }
  }

}