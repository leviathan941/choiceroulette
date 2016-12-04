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

import choiceroulette.gui.utils.CircleUtils

import scala.language.implicitConversions
import scalafx.beans.property.DoubleProperty
import scalafx.scene.Group
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape._

/** Choice arc for the roulette.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ChoiceArc(radius: Double,
                angleStart: Double,
                angleLength: Double,
                choiceText: String) extends StackPane {

  private val mArc = new Arc() {
    `type` = ArcType.Round
    radiusX = radius
    radiusY = radius
    startAngle = angleStart
    length = angleLength
    centerX = radius
    centerY = radius

    strokeLineCap = StrokeLineCap.Butt
    stroke = Color.Red
    strokeType = StrokeType.Inside
    fill = Color.Aquamarine
  }

  private val mBackRectangle = Rectangle(2 * radius, 2 * radius, Color.Transparent)

  private val mText = new ArcText(choiceText) {
    stroke = Color.Blue
  }

  private def getTextStartPoint(arc: Arc): (Double, Double) = {
    val arcStartAngleRad = math.toRadians(arc.startAngle)
    val acrLenAngleRad = math.toRadians(arc.length)
    val arcRadius: Double = arc.radiusX
    val arcCenter: (Double, Double) = (arc.centerX, arc.centerY)

    val circlePoint = CircleUtils.getCirclePoint(arcCenter, arcRadius, arcStartAngleRad + acrLenAngleRad / 2)

    CircleUtils.shiftPointAlongRadius(arcCenter, circlePoint, -20)
  }

  implicit def double2DoubleProperty(number: Double): DoubleProperty = DoubleProperty(number)
  implicit def doubleProperty2Double(property: DoubleProperty): Double = property.value

  def highlight(): Unit = {
    mArc.stroke = Color.White
  }

  def clearHighlight(): Unit = {
    mArc.stroke = Color.Red
  }

  mText.moveInsideArc(mArc, getTextStartPoint(mArc))

  children = new Group(mBackRectangle, mArc, mText)
  minHeight = 0
  minWidth = 0
  maxHeight = 2 * radius
  maxWidth = 2 * radius
}