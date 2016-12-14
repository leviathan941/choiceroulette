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

import scala.util.Random
import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.Node
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{FlowPane, Pane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, ArcType, Circle}

/** Pane for roulette view.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RoulettePane(prefController: PreferencesController,
                   actionController: ActionController,
                   radius: Double) extends Pane with PreferencesChangeListener with ActionListener { pane =>

  private lazy val mBackgroundCircle = new Circle() {
    radius = pane.radius + 3
    fill = Color.DarkSlateGrey
  }

  private lazy val mArcsPane = new ArcsPane(radius, 2)

  private lazy val mCursorArcPane = new FlowPane() {
    children = new Arc() {
      `type` = ArcType.Round
      radiusX = pane.radius / 10
      radiusY = pane.radius / 2
      startAngle = 175
      length = 10
      fill = Color.Black
    }

    maxWidth = 2 * radius
    maxHeight = 2 * radius
    alignment = Pos.CenterLeft
  }

  private lazy val mCenterCircle = new Circle() {
    radius = pane.radius / 10
    fill = Color.Black
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
    maxWidth = 2 * pane.radius
    maxHeight = 2 * pane.radius
  }

  private def showEditor(editor: EditChoiceField, loc: (Double, Double)): Unit = {
    children += editor

    editor.choiceArc.highlight()
    editor.relocate(mRouletteStack.layoutX.value + loc._1, mRouletteStack.layoutY.value + loc._2)
    editor.requestFocus()
  }

  override def onSpinAction(): Unit = {
    popupHider()

    val arcNumber = Random.nextInt(mArcsPane.arcsCount)
    showResult(mArcsPane.getArcText(arcNumber))
  }

  override def choiceCountChanged(count: Int): Unit = {
    mArcsPane.updateArcs(count)
  }

  private def showResult(result: String): Unit = {
    prefController.setPreferencesEnabled(enable = false)
    children += new SpinResultPane(result, width.value -> height.value, popupHider)
  }

  private val popupHider = () => {
    mArcsPane.clearHighlight()
    prefController.setPreferencesEnabled(enable = true)
    children = mRouletteStack
  }

  private def moveToPaneCenter(node: Node): Unit = {
    node.relocate(pane.width.value / 2 - radius, pane.height.value / 2 - radius)
  }

  height.onChange(moveToPaneCenter(mRouletteStack))
  width.onChange(moveToPaneCenter(mRouletteStack))

  prefController.listenPreferencesChange(this)
  actionController.listenActions(this)

  children = mRouletteStack
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"
}
