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

import choiceroulette.gui.roulette.data.DataHolder.ArcDataHolder
import choiceroulette.gui.roulette.data.{DataHoldable, RouletteDataController}
import choiceroulette.gui.utils.CircleUtils
import choiceroulette.gui.utils.Conversions._

import scalafx.scene.Group
import scalafx.scene.layout.StackPane
import scalafx.scene.paint.Color
import scalafx.scene.shape._

/** Choice arc for the roulette.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ChoiceArc(dataController: RouletteDataController,
                angleStart: Double,
                angleLength: Double) extends StackPane with DataHoldable {

  private class ArcHoldable extends Arc {
    `type` = ArcType.Round
    radiusX = dataController.rouletteData.wheelRadius
    radiusY = dataController.rouletteData.wheelRadius
    startAngle = angleStart
    length = angleLength
    centerX = dataController.rouletteData.wheelRadius
    centerY = dataController.rouletteData.wheelRadius

    strokeLineCap = StrokeLineCap.Butt
    strokeType = StrokeType.Inside
    stroke = Color.White
    strokeWidth = 2
    fill = Color.Aquamarine
    styleClass += "choice-arc"
  }

  private lazy val mArc = new ArcHoldable

  private lazy val mBackRectangle = Rectangle(2 * dataController.rouletteData.wheelRadius,
    2 * dataController.rouletteData.wheelRadius, Color.Transparent)

  private lazy val mTextLabel: ArcLabel =
      new ArcLabel(dataController, (angleStart, angleLength), () => getTextStartPoint, "Enter choice") {
    maxWidth = 0.6 * dataController.rouletteData.wheelRadius
  }

  private def getTextStartPoint: (Double, Double) = {
    val arcCenter: (Double, Double) = (mArc.centerX, mArc.centerY)

    CircleUtils.shiftPointAlongRadius(arcCenter, getChordCenterPoint(mArc), -25)
  }

  private def getChordCenterPoint(arc: Arc): (Double, Double) = {
    val arcStartAngleRad = math.toRadians(arc.startAngle)
    val acrLenAngleRad = math.toRadians(arc.length)
    val arcRadius: Double = arc.radiusX
    val arcCenter: (Double, Double) = (arc.centerX, arc.centerY)

    CircleUtils.getCirclePoint(arcCenter, arcRadius, arcStartAngleRad + acrLenAngleRad / 2)
  }

  private val mDataHolder: ArcDataHolder =
    new ArcDataHolder(mArc.fill, mArc.stroke, mArc.strokeWidth, mTextLabel.dataHolder)

  override def dataHolder: ArcDataHolder = mDataHolder
  def dataHolder_=(holder: ArcDataHolder): Unit = dataHolder.from(holder)

  def lowlight(): Unit = {
    val lowlightArc = new Arc {
      `type` = ArcType.Round
      radiusX = dataController.rouletteData.wheelRadius
      radiusY = dataController.rouletteData.wheelRadius
      startAngle = angleStart
      length = angleLength
      centerX = dataController.rouletteData.wheelRadius
      centerY = dataController.rouletteData.wheelRadius

      fill = Color.color(0, 0, 0, 0.65)
      styleClass += "arc-shading"
    }

    children = new Group(mBackRectangle, mArc, mTextLabel, lowlightArc)
  }

  def clearLowlight(): Unit = children = new Group(mBackRectangle, mArc, mTextLabel)

  children = new Group(mBackRectangle, mArc, mTextLabel)
  minHeight = 0
  minWidth = 0
  maxHeight = 2 * dataController.rouletteData.wheelRadius
  maxWidth = 2 * dataController.rouletteData.wheelRadius
}
