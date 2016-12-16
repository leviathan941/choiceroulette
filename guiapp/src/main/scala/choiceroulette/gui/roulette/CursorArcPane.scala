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

import scalafx.geometry.Pos
import scalafx.scene.layout.FlowPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, ArcType}

/** Cursor to point to won roulette arc.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class CursorArcPane(radius: Double) extends FlowPane {

  val positionAngle = 180

  children = new Arc() {
    `type` = ArcType.Round
    radiusX = radius / 10
    radiusY = radius / 2
    startAngle = 175
    length = 10
    fill = Color.Black
  }

  maxWidth = 2 * radius
  maxHeight = 2 * radius
  alignment = Pos.CenterLeft
}