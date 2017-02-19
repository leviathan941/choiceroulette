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

import choiceroulette.gui.controls.actions.{ActionController, ActionListener}
import choiceroulette.gui.roulette.EditChoiceField
import choiceroulette.gui.roulette.arc.ArcsController.ArcData
import choiceroulette.gui.roulette.data.{DataHolder, RouletteDataController}
import choiceroulette.gui.utils.CircleUtils
import scaldi.Injectable.inject
import scaldi.Injector

/** Controls arcs data and arc nodes.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcsController(dataController: RouletteDataController, actionController: ActionController)
                    (implicit val injector: Injector) {

  private var mArcsData: List[ArcData] = Nil
  private lazy val mArcsPane: ArcsPane = inject [ArcsPane]
  private val mActionListener = new ActionListener {
    override def onRefreshAction(): Unit = {
      mArcsPane.resetPane(mArcsData)
      actionController.actionButton = ActionController.ActionType.Spin
    }
    override def onSpinAction(): Unit = {}
  }

  def data(number: Int): ArcData = {
    require(number >= 0 && number < mArcsData.size, "Check arcs count first")
    mArcsData(number)
  }

  def textData: List[String] = mArcsData.map(_.arc.dataHolder.labelDataHolder.text)

  def fillPane(number: Int): Unit = {
    val curTexts = textData.take(number)
    val texts = curTexts ::: List.fill(number - curTexts.size)("Enter choice")
    fillPane(texts)
  }

  def fillPane(texts: List[String]): Unit = {
    val holders = mArcsData.map(_.arc.dataHolder)
    mArcsData.foreach(data => dataController.removeArcData(data.arc.dataHolder))

    mArcsData = if (texts.nonEmpty) createArcsData(texts, holders) else Nil
    dataController.rouletteData.arcsCount = texts.size

    if (!mArcsPane.isRotating)
      mArcsPane.resetPane(mArcsData)
    else
      actionController.actionButton = ActionController.ActionType.Refresh
  }

  def createEditor(loc: (Double, Double), onHide: () => Unit): Option[EditChoiceField] = {
    getArc(loc) match {
      case Some(arc) => Some(new EditChoiceField(arc.dataHolder.labelDataHolder, onHide, 50))
      case _ => None
    }
  }

  def clearHighlight(): Unit = mArcsPane.resetPane(mArcsData)

  def rotateArcToPoint(arcData: ArcData,
                       pointAngle: Double,
                       turns: Double,
                       angleCalc: (Double, Double, Double) => Double,
                       resultShower: () => Unit): Unit = {
    require(turns >= 0)

    mArcsPane.rotateArcToPoint(arcData, pointAngle, turns, angleCalc, () => {
      resultShower()
      mArcsPane.highlight(arcData)
    })
  }

  def applyColors(): Unit = applyColors(mArcsData)

  def removeArc(arcData: ArcData): Unit = removeArcInternal(arcData)()
  def removeArcAnimated(arcData: ArcData, onRemoved: () => Unit): Unit =
    new ArcFader(arcData.arc, removeArcInternal(arcData, onRemoved)).fade()

  private def count: Int = mArcsData.size

  private def createArcsData(textData: List[String], holders: List[DataHolder.ArcDataHolder]): List[ArcData] = {
    val arcsData = createRouletteArcs(textData)
    arcsData.foreach(data => {
      val prevHolder = holders.find(_.labelDataHolder.text == data.arc.dataHolder.labelDataHolder.text)
      if (prevHolder.isDefined)
        data.arc.dataHolder = prevHolder.get
      dataController.addArcData(data.arc.dataHolder)
    })
    applyColors(arcsData)
    arcsData
  }

  private def removeArcInternal(arcData: ArcData, onRemoved: () => Unit = () => {}): () => Unit =
      () => {
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

  private def createRouletteArcs(textData: List[String]): List[ArcData] = {
    val angles = CircleUtils.splitCircleToArcs(textData.size)

    angles.zip(angles.tail).zip(textData).
      map(data => { // ((startAngle, endAngle), text)
        ArcData(data._1._1, data._1._2,
          new ChoiceArc(dataController, data._1._1, data._1._2 - data._1._1, data._2))
    }).toList
  }

  private def applyColors(data: List[ArcData]): Unit =
    data.zip(arcColors(data.length)).foreach(data => data._1.arc.dataHolder.fill = data._2)

  private def arcColors(number: Int): List[Paint] =
    Stream.continually(dataController.arcFills.toStream).take(number).flatten.toList

  actionController.listenActions(mActionListener)
}

object ArcsController {
  case class ArcData(startAngle: Double, endAngle: Double, arc: ChoiceArc)
}
