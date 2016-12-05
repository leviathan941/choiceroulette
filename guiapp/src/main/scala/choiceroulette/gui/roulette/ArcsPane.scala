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

/** Stack pane containing choice arcs.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcsPane(private val radius: Double,
               arcsNumber: Int) extends StackPane {

  private var mArcsData: List[ArcData] = Nil

  def updateArcs(number: Int): Unit = {
    mArcsData = createRouletteSectors(number)
    children = mArcsData.map(_.arc)
  }

  def highlightArc(loc: (Double, Double)): Unit = {
    mArcsData.foreach(_.arc.clearHighlight())

    getArc(loc) match {
      case Some(arc) => arc.highlight()
      case _ =>
    }
  }

  private def getArc(loc: (Double, Double)): Option[ChoiceArc] = {
    val center = (radius, radius)
    val (r, phi) = CircleUtils.cartesianToPolar(loc, center)

    if (r > radius)
      None
    else
      findArc(if (phi > 0) 360 - math.toDegrees(phi) else -math.toDegrees(phi))
  }

  private def findArc(degrees: Double): Option[ChoiceArc] = {
    mArcsData.find(
      data => degrees > data.startAngle && degrees < data.endAngle
    ) match {
      case Some(arcData) => Some(arcData.arc)
      case _ => None
    }
  }

  private def createRouletteSectors(number: Int): List[ArcData] = {
    val angles = CircleUtils.splitCircleToSectors(number)

    angles.zip(angles.tail).map(arcAngle => {
      ArcData(arcAngle._1, arcAngle._2,
        new ChoiceArc(radius, arcAngle._1, arcAngle._2 - arcAngle._1, "Choice"))
    }).toList
  }

  private case class ArcData(startAngle: Double, endAngle: Double, arc: ChoiceArc)

  updateArcs(arcsNumber)
}
