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

import javafx.scene.paint.Paint

import choiceroulette.gui.roulette.data.RouletteDataController
import choiceroulette.gui.roulette.{EditChoiceField, RouletteRotator}
import choiceroulette.gui.utils.CircleUtils

import scalafx.scene.layout.StackPane

/** Pane containing choice arcs.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcsPane(dataController: RouletteDataController) extends StackPane(new ArcsPaneJfxDelegate) {

  case class ArcData(startAngle: Double, endAngle: Double, arc: ChoiceArc)
  private var mArcsData: List[ArcData] = Nil

  def fillPane(number: Int): Unit = {
    val holders = mArcsData.map(_.arc.dataHolder)
    mArcsData.foreach(data => dataController.removeArcData(data.arc.dataHolder))
    mArcsData = createRouletteSectors(number)
    mArcsData.zip(holders).foreach(tup => tup._1.arc.dataHolder = tup._2)
    applyCurrentColors()

    resetArcs()
  }

  def createEditor(loc: (Double, Double), onHide: () => Unit): Option[EditChoiceField] = {
    getArc(loc) match {
      case Some(arc) => Some(new EditChoiceField(arc.dataHolder.labelDataHolder, onHide, 50))
      case _ => None
    }
  }

  def arcsCount: Int = mArcsData.size

  def rotateArcToPoint(arcNumber:Int,
                       pointAngle: Double,
                       turns: Double,
                       angleCalc: (Double, Double, Double) => Double,
                       resultShower: String => Unit): Unit = {
    require(arcNumber >= 0 && arcNumber < mArcsData.size, "Check arcs count first")
    require(turns >= 0)

    val arcData = mArcsData(arcNumber)
    val angle = angleWithRotate(angleCalc(arcData.startAngle, arcData.endAngle, 5)) + turns * 360
    val rotator = new RouletteRotator(this, pointAngle + angle,
      () => resultShower(arcData.arc.dataHolder.labelDataHolder.text))
    rotator.spinTheWheel()
  }

  private def setArcColor(number: Int): javafx.scene.paint.Paint => Unit = color => {
    dataController.arcFills(number) = color
    applyCurrentColors()
  }

  private def applyCurrentColors(): Unit = {
    mArcsData.zip(arcColors(mArcsData.length)).foreach(data => data._1.arc.dataHolder.fill = data._2)
  }

  private def resetArcs(): Unit = {
    children = mArcsData.map(_.arc)
  }

  private def getArc(loc: (Double, Double)): Option[ChoiceArc] = {
    val center = (dataController.rouletteData.wheelRadius, dataController.rouletteData.wheelRadius)
    val (r, phi) = CircleUtils.cartesianToPolar(loc, center)

    if (r > dataController.rouletteData.wheelRadius)
      None
    else
      findArc(CircleUtils.fromMathTo0to2Pi(math.toDegrees(phi)))
  }

  private def findArc(degrees: Double): Option[ChoiceArc] = {
    val simplified = simplifiedRotate()

    mArcsData.find(
      data => {
        CircleUtils.simplifyAngle(degrees + simplified) > data.startAngle &&
          CircleUtils.simplifyAngle(degrees + simplified) < data.endAngle
      }
    ) match {
      case Some(arcData) => Some(arcData.arc)
      case _ => None
    }
  }

  private def createRouletteSectors(number: Int): List[ArcData] = {
    val angles = CircleUtils.splitCircleToSectors(number)

    angles.zip(angles.tail).map(initData => {
      ArcData(initData._1, initData._2,
        new ChoiceArc(dataController, initData._1, initData._2 - initData._1))
    }).toList
  }

  private def angleWithRotate(degrees: Double): Double = 360 - simplifiedRotate() + degrees

  private def simplifiedRotate(): Double = {
    rotate = CircleUtils.simplifyAngle(rotate.value)
    rotate.value
  }

  private def initDelegate(): Unit = {
    delegate match {
      case arcsDelegate: ArcsPaneJfxDelegate =>
        arcsDelegate.arcFirstColorProperty.setArcColor = this.setArcColor(0)
        arcsDelegate.arcSecondColorProperty.setArcColor = this.setArcColor(1)
      case _ =>
    }
  }

  private def arcColors(number: Int): List[Paint] =
    Stream.continually(dataController.arcFills.toStream).take(number).flatten.toList

  styleClass += "arcs-pane"
  initDelegate()
}
