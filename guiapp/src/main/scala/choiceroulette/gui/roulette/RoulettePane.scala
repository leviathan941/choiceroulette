/*
 * Copyright 2016, 2017 Alexey Kuzin <amkuzink@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package choiceroulette.gui.roulette

import choiceroulette.gui.controls.actions.{ActionController, ActionListener}
import choiceroulette.gui.controls.preferences.PreferencesController
import choiceroulette.gui.roulette.arc.{ArcsController, ArcsPane, CursorArcPane}
import choiceroulette.gui.roulette.data.DataHolder._
import choiceroulette.gui.roulette.data.RouletteDataController
import choiceroulette.gui.utils.CircleUtils
import scaldi.Injectable.inject

import scala.util.Random
import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Pane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape._

/** Pane for roulette view.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RoulettePane(prefController: PreferencesController,
                   actionController: ActionController,
                   dataController: RouletteDataController,
                   arcsController: ArcsController,
                   arcsPane: ArcsPane)
    extends Pane with ActionListener { pane =>

  implicit val injector = new RouletteInjector
  private var mWonArc: Option[ArcsController.ArcData] = None

  private lazy val mBackgroundCircle = new Circle() {
    radius = dataController.rouletteData.wheelRadius
    fill = Color.web("#212323")
    strokeType = StrokeType.Outside
    stroke = Color.web("#CCCCCC")
    strokeWidth = 3
    pickOnBounds = false
    styleClass += "background-circle"

    dataController.backgroundCircleData = Some(new BackgroundCircleDataHolder(
      fill, stroke, strokeWidth))

    strokeWidth.onChange(moveToPaneCenter(mRouletteStack))
  }

  private lazy val mArcsPane = arcsPane

  private lazy val mCursorArcPane = new CursorArcPane(dataController)

  private lazy val mCenterCircle = new Circle() {
    private lazy val mPressOffset: Double = 4

    radius = dataController.rouletteData.centerCircleRadius
    fill = Color.web("#36434C")
    stroke = Color.web("#919191")
    strokeType = StrokeType.Outside
    strokeWidth = 3
    styleClass += "center-circle"

    dataController.centerCircleData = Some(new CenterCircleDataHolder(fill))

    onMousePressed = handle {
      radius = radius.value - mPressOffset
    }
    onMouseReleased = handle {
      radius = radius.value + mPressOffset
      actionController.actionButton match {
        case ActionController.ActionType.Spin => actionController.spinRoulette()
        case ActionController.ActionType.Refresh => actionController.refreshArcs()
      }
    }
  }

  private class RouletteStack extends StackPane {

    onMouseClicked = (event: MouseEvent) => {
      if (event.clickCount == 2) {
        reset()
        arcsController.clearHighlight()

        val location = event.x -> event.y
        arcsController.createEditor(location, reset) match {
          case Some(editText) => showEditor(editText, location)
          case _ =>
        }
      }
    }

    children = mBackgroundCircle :: mArcsPane :: mCursorArcPane :: mCenterCircle :: Nil
    maxWidth = 2 * dataController.rouletteData.wheelRadius
    maxHeight = 2 * dataController.rouletteData.wheelRadius
    pickOnBounds = false
  }

  private var mRouletteStack = new RouletteStack

  private def showEditor(editor: EditChoiceField, loc: (Double, Double)): Unit = {
    children += editor

    editor.relocate(mRouletteStack.layoutX.value + loc._1, mRouletteStack.layoutY.value + loc._2)
    editor.requestFocus()
  }

  override def onSpinAction(): Unit = {

    doSpin(() => {
      reset()
      arcsController.clearHighlight()
      disableControls()

      val arcNumber = Random.nextInt(dataController.rouletteData.arcsCount)
      val arcData = arcsController.data(arcNumber)
      arcsController.rotateArcToPoint(arcData,
        mCursorArcPane.DEFAULT_POSITION_ANGLE,
        5,
        CircleUtils.randomAngleBetween,
        () => showResult(arcData))
    })
  }
  override def onRefreshAction(): Unit = {
    rouletteDataChanged(dataController.rouletteData)
  }

  private val rouletteDataChanged: RouletteDataHolder => Unit = holder => {
    mBackgroundCircle.radius = holder.wheelRadius
    mCenterCircle.radius = holder.centerCircleRadius
    mCursorArcPane.updateRadius(holder.wheelRadius)
    arcsController.fillPane(holder.arcsCount)
    mWonArc = None

    // Recreate stack to update its size
    mRouletteStack = new RouletteStack
    reset()
  }

  private def showResult(arcData: ArcsController.ArcData): Unit = {
    inject [ResultSaver].save(arcData.arc.dataHolder.labelDataHolder.text)
    mWonArc = Some(arcData)
    reset()
  }

  private def hoverPane(): Unit = {
    children += Rectangle(width.value, height.value, Color.Transparent)
  }

  private lazy val reset = () => {
    children = mRouletteStack
    dataController.rouletteData.listen(rouletteDataChanged)

    if (dataController.rouletteData.arcsCount < 2) {
      actionController.setActionsEnabled(enable = false)
      hoverPane()
    } else {
      setControlsEnabled()
    }

    moveToPaneCenter(mRouletteStack)
  }

  private def moveToPaneCenter(node: Node): Unit = {
    val fullRadius = dataController.rouletteData.wheelRadius + mBackgroundCircle.strokeWidth.value
    node.relocate(pane.width.value / 2 - fullRadius, pane.height.value / 2 - fullRadius)
  }

  private def setControlsEnabled(enabled: Boolean = true): Unit = {
    prefController.setPreferencesEnabled(enable = enabled)
    actionController.setActionsEnabled(enable = enabled)
  }

  private def doSpin(spin: () => Unit): Unit = {
    if (dataController.rouletteData.wonArcRemovable && mWonArc.isDefined &&
        dataController.rouletteData.arcsCount > 2) {
      disableControls()
      arcsController.removeArcAnimated(mWonArc.get, spin)
    }
    else spin()
  }

  private def disableControls(): Unit = {
    setControlsEnabled(enabled = false)
    hoverPane()
    dataController.rouletteData.ignore(rouletteDataChanged)
  }

  height.onChange(moveToPaneCenter(mRouletteStack))
  width.onChange(moveToPaneCenter(mRouletteStack))

  dataController.rouletteData.listen(rouletteDataChanged)
  actionController.listenActions(this)

  arcsController.fillPane(dataController.rouletteData.arcsCount)
  reset()

  dataController.restoreArcsData()
}
