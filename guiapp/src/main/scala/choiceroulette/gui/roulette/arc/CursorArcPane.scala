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
import scalafx.scene.shape.{Arc, ArcType, StrokeType}

/** Cursor to point to won roulette arc.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class CursorArcPane(dataController: RouletteDataController) extends FlowPane {

  val DEFAULT_POSITION_ANGLE = 180

  private val mArc = new Arc() {
    `type` = ArcType.Round
    startAngle = 170
    length = 20
    fill = Color.Black
    strokeType = StrokeType.Inside
    styleClass += "wheel-cursor"

    dataController.cursorArcData = Some(new CursorArcDataHolder(fill))
  }

  def updateRadius(radius: Double): Unit = {
    maxWidth = 2 * radius
    maxHeight = 2 * radius
    mArc.radiusX = radius / 10
    mArc.radiusY = radius / 2
  }

  updateRadius(dataController.rouletteData.wheelRadius)

  children = mArc
  alignment = Pos.CenterLeft
}
