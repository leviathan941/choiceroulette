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

import choiceroulette.gui.utils.CircleUtils
import choiceroulette.gui.utils.Conversions._

import scalafx.scene.Group
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.PaintIncludes._
import scalafx.scene.paint.{Color, Paint}
import scalafx.scene.shape._

/** Choice arc for the roulette.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ChoiceArc(radius: Double,
                angleStart: Double,
                angleLength: Double) extends StackPane {

  private lazy val mArc = new Arc() {
    `type` = ArcType.Round
    radiusX = radius
    radiusY = radius
    startAngle = angleStart
    length = angleLength
    centerX = radius
    centerY = radius

    strokeLineCap = StrokeLineCap.Butt
    strokeType = StrokeType.Inside
    stroke = Color.White
    strokeWidth = 2
    fill = Color.Aquamarine
    styleClass += "choice-arc"
  }

  private lazy val mBackRectangle = Rectangle(2 * radius, 2 * radius, Color.Transparent)

  private lazy val mText = new ArcLabel(mArc, getTextStartPoint, "Enter choice") {
    maxWidth = 0.6 * radius
  }

  private lazy val getTextStartPoint: Arc => (Double, Double) = arc => {
    val arcCenter: (Double, Double) = (arc.centerX, arc.centerY)

    CircleUtils.shiftPointAlongRadius(arcCenter, getChordCenterPoint(arc), -25)
  }

  private def getChordCenterPoint(arc: Arc): (Double, Double) = {
    val arcStartAngleRad = math.toRadians(arc.startAngle)
    val acrLenAngleRad = math.toRadians(arc.length)
    val arcRadius: Double = arc.radiusX
    val arcCenter: (Double, Double) = (arc.centerX, arc.centerY)

    CircleUtils.getCirclePoint(arcCenter, arcRadius, arcStartAngleRad + acrLenAngleRad / 2)
  }

  def text: String = mText.text.value

  def text_=(text: String): Unit = {
    if (!text.isEmpty)
      mText.text = text
  }

  def color: Paint = mArc.fill.value

  def color_=(paint: Paint): Unit = {
    mArc.fill = paint
  }

  children = new Group(mBackRectangle, mArc, mText)
  minHeight = 0
  minWidth = 0
  maxHeight = 2 * radius
  maxWidth = 2 * radius
}
