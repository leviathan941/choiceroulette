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

import scalafx.scene.layout.StackPane

/** Pane containing choice arcs.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcsPane(radius: Double, arcsNumber: Int) extends StackPane {

  private var mArcsData: List[ArcData] = Nil

  def fillPane(number: Int): Unit = {
    mArcsData = createRouletteSectors(number)
    resetArcs()
  }

  def highlightArc(loc: (Double, Double)): Unit = {
    clearHighlight()

    getArc(loc) match {
      case Some(arc) => arc.highlight()
      case _ =>
    }
  }

  def clearHighlight(): Unit = {
    mArcsData.foreach(_.arc.clearHighlight())
  }

  def createEditor(loc: (Double, Double), onHide: () => Unit): Option[EditChoiceField] = {
    getArc(loc) match {
      case Some(arc) => Some(new EditChoiceField(arc, onHide, 50))
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
    val rotator = new RouletteRotator(this, pointAngle + angle, () => resultShower(arcData.arc.text))
    rotator.spinTheWheel()
  }

  private def resetArcs(): Unit = {
    children = mArcsData.map(_.arc)
  }

  private def getArc(loc: (Double, Double)): Option[ChoiceArc] = {
    val center = (radius, radius)
    val (r, phi) = CircleUtils.cartesianToPolar(loc, center)

    if (r > radius)
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

    angles.zip(angles.tail).map(arcAngle => {
      ArcData(arcAngle._1, arcAngle._2,
        new ChoiceArc(radius, arcAngle._1, arcAngle._2 - arcAngle._1, "Enter choice"))
    }).toList
  }

  private def angleWithRotate(degrees: Double): Double = 360 - simplifiedRotate() + degrees

  private def simplifiedRotate(): Double = {
    rotate = CircleUtils.simplifyAngle(rotate.value)
    rotate.value
  }

  case class ArcData(startAngle: Double, endAngle: Double, arc: ChoiceArc)

  fillPane(arcsNumber)
}
