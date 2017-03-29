package com.erikcupal.theblackcatclient.gameObjects

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.erikcupal.theblackcatclient.*
import com.erikcupal.theblackcatclient.core.GameCore
import com.erikcupal.theblackcatclient.gui.GroupBase
import com.erikcupal.theblackcatclient.helpers.*
import com.erikcupal.theblackcatclient.screens.Objects.CardsContainer
import com.erikcupal.theblackcatclient.types.*
import ktx.actors.alpha

class CardObject(
  game: GameCore,
  var owner: PlayerSide? = null,
  var canBeSelected: (() -> Boolean)? = null,
  var canBePlayed: (() -> Boolean)? = null
) : GroupBase(game), ICard {

  override var suit: Suit? = null
  override var rank: Rank? = null

  var baseScale = CARD_INITIAL_SCALE
  var magnification = 1f
  val magnifyDuration = 0.15f
  var customZIndex = 0

  var transformationsCount = 0

  var scaleAction: ScaleToAction? = null
    set(value) {
      this removeAction scaleAction
      field = value
      this addAction value
    }

  var floating = false
    set(value) {
      if (value) {
        scaleAction = null
      }
      field = value
    }

  var magnify = false
    set(value) {
      if (!floating) {
        when (value) {
          true  -> scaleAction = scaleToAction(
            scale = baseScale * magnification,
            duration = magnifyDuration,
            interpolation = fade
          )
          false -> scaleAction = scaleToAction(
            scale = baseScale,
            duration = magnifyDuration,
            interpolation = fade
          )
        }
      }
      field = value
    }

  private var cardImage = Image(getCurrentCardDrawable())
  private var cardSelectionImage = Image(assets.getDrawable("cards/selection"))

  var isSelected: Boolean = false
    set(value) {
      field = value

      when (value) {
        false -> cardSelectionImage.hide(0.2f)
        true  -> cardSelectionImage.show(0.2f)
      }
    }


  init {
    touchable = Touchable.enabled
    cardSelectionImage.alpha = 0f
    setOrigin(0f, 0f)
    setScale(baseScale)

    this += cardImage
    this += cardSelectionImage

    configureMouseEvents()
  }

  private fun configureMouseEvents() {

    this onMouseEnter { if(!floating) magnify = true }
    this onMouseExit { if(!floating) magnify = false }

    this onTap {
      if (canBeSelected != null) {
        when (isSelected) {
          false -> {
            if (canBeSelected!!()) {
              isSelected = true
              dispatch..GAME_CARD_SELECTION_CHANGED()
            }
          }
          true  -> {
            isSelected = false
            dispatch..GAME_CARD_SELECTION_CHANGED()
          }
        }
      }

      if (canBePlayed != null && canBePlayed!!()) {
        dispatch..GAME_PLAY_CARD(this)
      }
    }
  }

  private fun updateCardImage() {
    cardImage.drawable = getCurrentCardDrawable()
  }

  private fun getCurrentCardDrawable(): Drawable {
    val drawableName = when (suit != null && rank != null) {
      true  -> {
        val suitName = suit!!.name.toLowerCase()
        val rankName = rank!!.name.toLowerCase()

        "cards/$suitName/${suitName}_$rankName"
      }
      false -> "cards/back"
    }

    return assets.getDrawable(drawableName)
  }


  fun transform(transformation: CardTransformation, duration: Float, delay: Float = 0f, valueChange: CardValueChange? = null) {

    baseScale = transformation.scale.x

    magnify = false

    this addAction delay(
      time = delay,
      action = parallel(
        moveToAction(
          x = transformation.position.x,
          y = transformation.position.y,
          duration = duration,
          interpolation = fade
        ),
        rotateToAction(
          rotation = transformation.rotation,
          duration = duration,
          interpolation = fade
        ),
        if (didCardChangeValue(valueChange)) {
          sequence(
            doAction {
              transformationsCount++
              floating = true
            },
            if (!transformation.noScaling) {
              scaleToAction(
                scaleX = 0f,
                scaleY = transformation.scale.y,
                duration = duration / 2,
                interpolation = Interpolation.pow2In)
            } else {
              delay(duration / 2)
            },
            doAction {
              zIndex = transformation.zIndex
              customZIndex = transformation.zIndex
              magnification = transformation.magnification

              if (parent is CardsContainer) {
                (parent as CardsContainer).order()
              }

              suit = valueChange!!.suit
              rank = valueChange!!.rank
              updateCardImage()
            },
            if (!transformation.noScaling) {
              scaleToAction(
                scaleX = transformation.scale.x,
                scaleY = transformation.scale.y,
                duration = duration / 2,
                interpolation = Interpolation.pow2Out)
            } else {
              delay(duration / 2)
            },
            doAction {
              transformationsCount--
              floating = false
            }
          )
        } else {
          sequence(
            doAction {
              transformationsCount++
              floating = true
            },
            parallel(
              if (!transformation.noScaling) {
                scaleToAction(
                  scaleX = transformation.scale.x,
                  scaleY = transformation.scale.y,
                  duration = duration / 2,
                  interpolation = fade)
              } else {
                delay(duration / 2)
              },
              delay(
                time = duration / 2,
                action = doAction {
                  zIndex = transformation.zIndex
                  customZIndex = transformation.zIndex
                  magnification = transformation.magnification

                  if (parent is CardsContainer) {
                    (parent as CardsContainer).order()
                  }
                }
              )
            ),
            doAction {
              transformationsCount--
              floating = false
            }
          )
        }
      )
    )

  }

  private fun didCardChangeValue(valueChange: CardValueChange?): Boolean {
    return valueChange != null
      && !(valueChange.suit == suit && valueChange.rank == rank)
  }
}