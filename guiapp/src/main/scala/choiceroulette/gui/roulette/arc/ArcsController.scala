/*
 * Copyright 2017 Alexey Kuzin <amkuzink@gmail.com>
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

import choiceroulette.gui.roulette.EditChoiceField
import choiceroulette.gui.roulette.arc.ArcsController.ArcData
import choiceroulette.gui.roulette.data.RouletteDataController
import choiceroulette.gui.utils.CircleUtils
import scaldi.Injectable.inject
import scaldi.Injector

/** Controls arcs data and arc nodes.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcsController(dataController: RouletteDataController)(implicit val injector: Injector) {

  private var mArcsData: List[ArcData] = Nil
  private lazy val mArcsPane: ArcsPane = inject [ArcsPane]

  def count: Int = mArcsData.size

  def data(number: Int): ArcData = {
    require(number >= 0 && number < mArcsData.size, "Check arcs count first")
    mArcsData(number)
  }

  def fillPane(number: Int): Unit = {
    val holders = mArcsData.map(_.arc.dataHolder)
    mArcsData.foreach(data => dataController.removeArcData(data.arc.dataHolder))

    if (number >= 2) {
      mArcsData = createRouletteArcs(number)
      mArcsData.foreach(data => dataController.addArcData(data.arc.dataHolder))
      mArcsData.zip(holders).foreach(tup => tup._1.arc.dataHolder = tup._2)
      applyColors()
    } else {
      mArcsData = Nil
    }

    mArcsPane.resetPane(mArcsData)
  }

  def createEditor(loc: (Double, Double), onHide: () => Unit): Option[EditChoiceField] = {
    getArc(loc) match {
      case Some(arc) => Some(new EditChoiceField(arc.dataHolder.labelDataHolder, onHide, 50))
      case _ => None
    }
  }

  def highlight(arcNumber: Int): Unit = {
    require(arcNumber >= 0 && arcNumber < mArcsData.size, "Check arcs count first")

    val arcData = mArcsData(arcNumber)
    mArcsPane.highlight(arcData)
  }

  def clearHighlight(): Unit = mArcsPane.resetPane(mArcsData)

  def rotateArcToPoint(arcNumber: Int,
                       pointAngle: Double,
                       turns: Double,
                       angleCalc: (Double, Double, Double) => Double,
                       resultShower: () => Unit): Unit = {
    require(arcNumber >= 0 && arcNumber < mArcsData.size, "Check arcs count first")
    require(turns >= 0)

    val arcData = mArcsData(arcNumber)
    mArcsPane.rotateArcToPoint(arcData, pointAngle, turns, angleCalc, resultShower)
  }

  def applyColors(): Unit =
    mArcsData.zip(arcColors(mArcsData.length)).foreach(data => data._1.arc.dataHolder.fill = data._2)

  def removeArc(arcNumber: Int): Unit = {
    require(arcNumber >= 0 && arcNumber < mArcsData.size, "Check arcs count first")

    removeArcInternal(arcNumber)()
  }

  def removeArcAnimated(arcNumber: Int, onRemoved: () => Unit): Unit = {
    require(arcNumber >= 0 && arcNumber < mArcsData.size, "Check arcs count first")

    val arcData = mArcsData(arcNumber)
    new ArcFader(arcData.arc, removeArcInternal(arcNumber, onRemoved)).fade()
  }

  private def removeArcInternal(arcNumber: Int, onRemoved: () => Unit = () => {}): () => Unit =
      () => {
    val arcData = mArcsData(arcNumber)
    dataController.removeArcData(arcData.arc.dataHolder)
    mArcsData = mArcsData.filter(_ != arcData)
    dataController.rouletteData.arcsCount = count
    onRemoved()
  }

  private def findArc(degrees: Double): Option[ChoiceArc] = {
    val simplified = mArcsPane.simplifiedRotate()

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

  private def getArc(loc: (Double, Double)): Option[ChoiceArc] = {
    val center = (dataController.rouletteData.wheelRadius, dataController.rouletteData.wheelRadius)
    val (r, phi) = CircleUtils.cartesianToPolar(loc, center)

    if (r > dataController.rouletteData.wheelRadius)
      None
    else
      findArc(CircleUtils.fromMathTo0to2Pi(math.toDegrees(phi)))
  }

  private def createRouletteArcs(number: Int): List[ArcData] = {
    val angles = CircleUtils.splitCircleToArcs(number)

    angles.zip(angles.tail).map(initData => {
      ArcData(initData._1, initData._2,
        new ChoiceArc(dataController, initData._1, initData._2 - initData._1))
    }).toList
  }

  private def arcColors(number: Int): List[Paint] =
    Stream.continually(dataController.arcFills.toStream).take(number).flatten.toList
}

object ArcsController {
  case class ArcData(startAngle: Double, endAngle: Double, arc: ChoiceArc)
}
