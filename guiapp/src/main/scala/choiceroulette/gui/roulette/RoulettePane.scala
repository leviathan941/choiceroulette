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
import choiceroulette.gui.utils.CircleUtils

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{FlowPane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, ArcType, Circle}

/** Pane for roulette view.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RoulettePane(private val radius: Double) extends StackPane with PreferencesChangeListener { pane =>

  private val mBackgroundCircle = new Circle() {
    radius = pane.radius
    fill = Color.Black
  }

  private val mCursorArcPane = new FlowPane() {
    children = new Arc() {
      `type` = ArcType.Round
      radiusX = pane.radius / 6
      radiusY = pane.radius / 2
      startAngle = 170
      length = 20
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

  private def createRouletteSectors(number: Int): List[ChoiceArc] = {
    val angles = CircleUtils.splitCircleToSectors(number)
    for (idx <- angles.indices.toList
      if idx != 0 && idx < angles.size;
      startAngle = angles(idx - 1);
      angleLen = angles(idx) - startAngle
    ) yield new ChoiceArc(pane.radius, startAngle, angleLen, "Choice " + idx)
  }

  override def choiceCountChanged(count: Int): Unit = {
    children = mBackgroundCircle :: createRouletteSectors(count) ::: mCenterCircle :: mCursorArcPane :: Nil
  }

  children = mBackgroundCircle :: createRouletteSectors(2) ::: mCenterCircle :: mCursorArcPane :: Nil

  alignment = Pos.Center
  padding = Insets(10)
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"
}
