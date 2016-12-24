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
import choiceroulette.gui.controls.preferences.{PreferencesChangeListener, PreferencesController}
import choiceroulette.gui.roulette.arc.{ArcsPane, CursorArcPane}
import choiceroulette.gui.roulette.data.DataHolder.{BackgroundCircleDataHolder, CenterCircleDataHolder}
import choiceroulette.gui.roulette.data.RouletteDataController
import choiceroulette.gui.utils.CircleUtils

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
    extends Pane with PreferencesChangeListener with ActionListener { pane =>

  private lazy val mBackgroundCircle = new Circle() {
    radius = dataController.rouletteData.radius
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
    radius = dataController.rouletteData.radius / 5
    fill = Color.DarkGray
    styleClass += "center-circle"

    dataController.centerCircleData = Some(new CenterCircleDataHolder(fill))
  }

  private lazy val mRouletteStack: StackPane = new StackPane() {

    onMouseClicked = (event: MouseEvent) => {
      if (event.clickCount == 2) {
        popupHider()

        val location = event.x -> event.y
        mArcsPane.createEditor(location, popupHider) match {
          case Some(editText) => showEditor(editText, location)
          case _ =>
        }
      }
    }

    children = mBackgroundCircle :: mArcsPane :: mCenterCircle :: mCursorArcPane :: Nil
    maxWidth = 2 * dataController.rouletteData.radius
    maxHeight = 2 * dataController.rouletteData.radius
  }

  private def showEditor(editor: EditChoiceField, loc: (Double, Double)): Unit = {
    children += editor

    editor.relocate(mRouletteStack.layoutX.value + loc._1, mRouletteStack.layoutY.value + loc._2)
    editor.requestFocus()
  }

  override def onSpinAction(): Unit = {
    popupHider()
    setControlsEnabled(enabled = false)
    hoverPane()

    val arcNumber = Random.nextInt(mArcsPane.arcsCount)
    mArcsPane.rotateArcToPoint(arcNumber, mCursorArcPane.positionAngle, 4, CircleUtils.randomAngleBetween, showResult)
  }

  override def choiceCountChanged(count: Int): Unit = {
    mArcsPane.fillPane(count)
  }

  private val showResult: String => Unit = (result: String) => {
    children += new SpinResultPane(result, width.value -> height.value, popupHider)
  }

  private def hoverPane(): Unit = {
    children += Rectangle(width.value, height.value, Color.Transparent)
  }

  private lazy val popupHider = () => {
    setControlsEnabled()
    children = mRouletteStack
  }

  private def moveToPaneCenter(node: Node): Unit = {
    node.relocate(pane.width.value / 2 - dataController.rouletteData.radius,
      pane.height.value / 2 - dataController.rouletteData.radius)
  }

  private def setControlsEnabled(enabled: Boolean = true): Unit = {
    prefController.setPreferencesEnabled(enable = enabled)
    actionController.setActionsEnabled(enable = enabled)
  }

  height.onChange(moveToPaneCenter(mRouletteStack))
  width.onChange(moveToPaneCenter(mRouletteStack))

  prefController.listenPreferencesChange(this)
  actionController.listenActions(this)

  children = mRouletteStack
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"

  mArcsPane.fillPane(2)
}
