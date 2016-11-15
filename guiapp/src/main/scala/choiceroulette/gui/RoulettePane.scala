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

package choiceroulette.gui

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{FlowPane, StackPane}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, ArcType, Circle}

/**
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RoulettePane extends StackPane {

  private val mBackgroundCircle = new Circle() {
    radius = 200
    fill = Color.Black
  }

  private val mCursorArcPane = new FlowPane() {
    children = new Arc() {
      `type` = ArcType.Round
      radiusX = 35
      radiusY = 100
      startAngle = 170
      length = 20
      fill = Color.White
    }

    maxWidth = 400
    maxHeight = 400
    alignment = Pos.CenterLeft
  }

  private val mCenterCircle = new Circle() {
    radius = 20
    fill = Color.White
  }

  children = List(mBackgroundCircle, mCursorArcPane, mCenterCircle)

  alignment = Pos.Center
  margin = Insets(10)
}
