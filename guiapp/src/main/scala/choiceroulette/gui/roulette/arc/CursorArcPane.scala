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

package choiceroulette.gui.roulette.arc

import choiceroulette.gui.roulette.data.DataHolder.CursorArcDataHolder
import choiceroulette.gui.roulette.data.RouletteDataController

import scalafx.geometry.Pos
import scalafx.scene.layout.FlowPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, ArcType}

/** Cursor to point to won roulette arc.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class CursorArcPane(dataController: RouletteDataController) extends FlowPane {

  val positionAngle = 180

  private val mArc = new Arc() {
    `type` = ArcType.Round
    radiusX = dataController.rouletteData.radius / 10
    radiusY = dataController.rouletteData.radius / 2
    startAngle = 170
    length = 20
    fill = Color.Black
    styleClass += "wheel-cursor"

    dataController.cursorArcData = Some(new CursorArcDataHolder(fill))
  }
  children = mArc

  maxWidth = 2 * dataController.rouletteData.radius
  maxHeight = 2 * dataController.rouletteData.radius
  alignment = Pos.CenterLeft
}
