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

  private val mText = new UpdatableText(choiceText) {
    stroke = Color.Blue
  }

  private def getTextPoint(arc: Arc): (Double, Double) = {
    val arcStartAngleRad = math.toRadians(arc.startAngle)
    val acrLenAngleRad = math.toRadians(arc.length)
    val arcRadius: Double = arc.radiusX
    val arcCenter: (Double, Double) = (arc.centerX, arc.centerY)

    val p1 = getCirclePointCoordinates(arcCenter, arcRadius, arcStartAngleRad)
    val p2 = getCirclePointCoordinates(arcCenter, arcRadius, arcStartAngleRad + acrLenAngleRad)

    getTextStartPoint(p1, p2)
  }

  private def getTextStartPoint(p1: (Double, Double),
                                p2: (Double, Double)): (Double, Double) = {
    val x = (p1._1 + p2._1) / 2
    val y = (p1._2 + p2._2) / 2
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

  mText.moveInsideArc(mArc)

  children = new Group(mBackRectangle, mArc, mText)
  minHeight = 0
  minWidth = 0
  maxHeight = 2 * radius
  maxWidth = 2 * radius

  private class UpdatableText(text: String) extends Text(text) { thisText =>

    def moveInsideArc(arc: Arc): Unit = {
      val angle = math.toRadians(rotateTextToArc(arc))
      val (x, y) = getTextPoint(arc)

      val shift = thisText.layoutBounds.value.getHeight / 2
      val dx = shift * math.sin(angle)
      val dy = shift * math.cos(angle)
      relocate(x + dx, y - dy)
    }

    private def rotateTextToArc(arc: Arc): Double = {
      val angle = 180 - arc.startAngle.value - arc.length.value / 2
      transforms.add(new Rotate(angle, layoutBounds.value.getMinX, layoutBounds.value.getMinY))
      angle
    }
  }
}
