/*
 * Copyright 2016 Alexey Kuzin <amkuzink@gmail.com>
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
import choiceroulette.gui.roulette.arc.{ArcsPane, CursorArcPane}
import choiceroulette.gui.roulette.data.DataHolder.{BackgroundCircleDataHolder, CenterCircleDataHolder,
    RouletteDataHolder}
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
                   dataController: RouletteDataController)
    extends Pane with ActionListener { pane =>

  implicit val injector = new RouletteInjector

  private lazy val mBackgroundCircle = new Circle() {
    radius = dataController.rouletteData.wheelRadius
    fill = Color.White
    strokeType = StrokeType.Outside
    stroke = Color.Black
    strokeWidth = 3
    styleClass += "background-circle"

    dataController.backgroundCircleData = Some(new BackgroundCircleDataHolder(
      fill, stroke, strokeWidth))
  }

  private lazy val mArcsPane = new ArcsPane(dataController)

  private lazy val mCursorArcPane = new CursorArcPane(dataController)

  private lazy val mCenterCircle = new Circle() {

    private lazy val mPressOffset: Double = 4

    radius = dataController.rouletteData.centerCircleRadius
    fill = Color.DarkGray
    styleClass += "center-circle"

    dataController.centerCircleData = Some(new CenterCircleDataHolder(fill))

    onMousePressed = handle {
      radius = radius.value - mPressOffset
    }
    onMouseReleased = handle {
      radius = radius.value + mPressOffset
      onSpinAction()
    }
  }

  private class RouletteStack extends StackPane {

    onMouseClicked = (event: MouseEvent) => {
      if (event.clickCount == 2) {
        reset()
        mArcsPane.clearHighlight()

        val location = event.x -> event.y
        mArcsPane.createEditor(location, reset) match {
          case Some(editText) => showEditor(editText, location)
          case _ =>
        }
      }
    }

    children = mBackgroundCircle :: mArcsPane :: mCursorArcPane :: mCenterCircle :: Nil
    maxWidth = 2 * dataController.rouletteData.wheelRadius
    maxHeight = 2 * dataController.rouletteData.wheelRadius
  }

  private var mRouletteStack = new RouletteStack

  private def showEditor(editor: EditChoiceField, loc: (Double, Double)): Unit = {
    children += editor

    editor.relocate(mRouletteStack.layoutX.value + loc._1, mRouletteStack.layoutY.value + loc._2)
    editor.requestFocus()
  }

  override def onSpinAction(): Unit = {
    reset()
    mArcsPane.clearHighlight()
    setControlsEnabled(enabled = false)
    hoverPane()

    val arcNumber = Random.nextInt(mArcsPane.arcsCount)
    mArcsPane.rotateArcToPoint(arcNumber,
      mCursorArcPane.DEFAULT_POSITION_ANGLE,
      5,
      CircleUtils.randomAngleBetween,
      () => showResult(arcNumber))
  }

  private val rouletteDataChanged: RouletteDataHolder => Unit = holder => {
    mBackgroundCircle.radius = holder.wheelRadius
    mCenterCircle.radius = holder.centerCircleRadius
    mCursorArcPane.updateRadius(holder.wheelRadius)
    mArcsPane.fillPane(holder.arcsCount)

    // Recreate stack to update its size
    mRouletteStack = new RouletteStack
    reset()
  }

  private def showResult(arcNumber: Int): Unit = {
    inject [ResultSaver].save(dataController.arcData(arcNumber).labelDataHolder.text)
    mArcsPane.highlight(arcNumber)
    reset()
  }

  private def hoverPane(): Unit = {
    children += Rectangle(width.value, height.value, Color.Transparent)
  }

  private lazy val reset = () => {
    setControlsEnabled()
    children = mRouletteStack
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

  height.onChange(moveToPaneCenter(mRouletteStack))
  width.onChange(moveToPaneCenter(mRouletteStack))

  dataController.rouletteData.listen(rouletteDataChanged)
  actionController.listenActions(this)

  children = mRouletteStack
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"

  mArcsPane.fillPane(dataController.rouletteData.arcsCount)
  dataController.restoreArcsData()
}
