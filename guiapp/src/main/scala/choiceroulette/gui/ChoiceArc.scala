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

import scala.language.implicitConversions
import scalafx.beans.property.DoubleProperty
import scalafx.scene.Group
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape._
import scalafx.scene.text.Text
import scalafx.scene.transform.Rotate

/** Choice arc for the roulette.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ChoiceArc(radius: Double,
                angleStart: Double,
                angleLength: Double) extends StackPane {

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

  private val mText = new UpdatableText("Choice") {
    stroke = Color.Blue
  }

  private def getArcCenter(arc: Arc): (Double, Double) = {
    val arcStartAngleRad = math.toRadians(arc.startAngle)
    val acrLenAngleRad = math.toRadians(arc.length)
    val arcRadius: Double = arc.radiusX
    val arcCenter: (Double, Double) = (arc.centerX, arc.centerY)

    val p1 = getCirclePointCoordinates(arcCenter, arcRadius, arcStartAngleRad)
    val p2 = getCirclePointCoordinates(arcCenter, arcRadius, arcStartAngleRad + acrLenAngleRad)

    getArcCenter(arcCenter, p2, p1)
  }

  private def getArcCenter(p1: (Double, Double),
                           p2: (Double, Double),
                           p3: (Double, Double)): (Double, Double) = {
    val x = (p1._1 + p2._1 + p3._1) / 3
    val y = (p1._2 + p2._2 + p3._2) / 3
    (x, y)
  }

  private def getCirclePointCoordinates(center: (Double, Double),
                                        radius: Double,
                                        angle: Double): (Double, Double) = {
    (center._1 + radius * math.cos(angle),
      center._2 - radius * math.sin(angle))
  }

  implicit def double2DoubleProperty(number: Double): DoubleProperty = DoubleProperty(number)
  implicit def doubleProperty2Double(property: DoubleProperty): Double = property.value

  mText.moveToArcCenter(mArc)
  mText.rotateTextToArc(mArc)

  children = new Group(mBackRectangle, mArc, mText)
  minHeight = 0
  minWidth = 0
  maxHeight = 2 * radius
  maxWidth = 2 * radius

  private class UpdatableText(text: String) extends Text(text) { thisText =>

    def moveToArcCenter(arc: Arc): Unit = {
      val (x, y) = getArcCenter(arc)
      relocate(x, y)
    }

    def rotateTextToArc(arc: Arc): Unit = {
      val angle = -(arc.startAngle.value + arc.length.value / 2)
      transforms.add(new Rotate(angle, 0, 0))
    }
  }
}
