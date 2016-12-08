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

import choiceroulette.gui.preferences.PreferencesChangeListener

import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{FlowPane, Pane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, ArcType, Circle}

/** Pane for roulette view.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RoulettePane(radius: Double) extends Pane with PreferencesChangeListener { pane =>

  private val mBackgroundCircle = new Circle() {
    radius = pane.radius
    fill = Color.Black
  }

  private val mArcsPane = new ArcsPane(radius, 2)

  private val mCursorArcPane = new FlowPane() {
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

  private val mCenterCircle = new Circle() {
    radius = pane.radius / 10
    fill = Color.Black
  }

  private val mRouletteStack = new StackPane() {

    def moveToParentCenter(): Unit = {
      relocate(pane.width.value / 2 - radius, pane.height.value / 2 - radius)
    }

    onMouseClicked = (event: MouseEvent) => {
      if (event.clickCount == 2) {
        mArcsPane.clearHighlight()

        val location = event.x -> event.y
        mArcsPane.createEditor(location, hideEditor) match {
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
    children = mRouletteStack :: editor :: Nil

    editor.choiceArc.highlight()
    editor.relocate(mRouletteStack.layoutX.value + loc._1, mRouletteStack.layoutY.value + loc._2)
    editor.requestFocus()
  }

  private def hideEditor(): Unit = {
    mArcsPane.clearHighlight()
    children = mRouletteStack
  }

  override def choiceCountChanged(count: Int): Unit = {
    mArcsPane.updateArcs(count)
  }

  height.onChange(mRouletteStack.moveToParentCenter())
  width.onChange(mRouletteStack.moveToParentCenter())

  children = mRouletteStack
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"
}
