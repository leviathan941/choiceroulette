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

import choiceroulette.gui.roulette.RouletteRotator
import choiceroulette.gui.roulette.arc.ArcsController.ArcData
import choiceroulette.gui.roulette.data.RouletteDataController
import choiceroulette.gui.utils.CircleUtils
import scaldi.Injector
import scaldi.Injectable.inject

import scalafx.Includes._
import scalafx.scene.layout.StackPane

/** Pane containing choice arcs.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcsPane(dataController: RouletteDataController)(implicit val injector: Injector)
    extends StackPane(new ArcsPaneJfxDelegate) {

  def resetPane(arcsData: List[ArcData]): Unit = {
    children = if (arcsData.size >= 2) arcsData.map(_.arc) else Nil
  }

  private[arc] def rotateArcToPoint(arcData: ArcData,
                       pointAngle: Double,
                       turns: Double,
                       angleCalc: (Double, Double, Double) => Double,
                       resultShower: () => Unit): Unit = {
    val angle = angleWithRotate(angleCalc(arcData.startAngle, arcData.endAngle, 2)) + turns * 360
    val rotator = new RouletteRotator(this, pointAngle + angle, resultShower)
    rotator.spinTheWheel()
  }

  private[arc] def highlight(arcData: ArcData): Unit =
    children += new ShadingArc(dataController.rouletteData.wheelRadius, (arcData.startAngle, arcData.endAngle))

  def simplifiedRotate(): Double = {
    rotate = CircleUtils.simplifyAngle(rotate.value)
    rotate.value
  }

  private def setArcColor(number: Int): javafx.scene.paint.Paint => Unit = color => {
    dataController.arcFills(number) = color
    inject [ArcsController].applyColors()
  }

  private def angleWithRotate(degrees: Double): Double = 360 - simplifiedRotate() + degrees

  private def initDelegate(): Unit = {
    delegate match {
      case arcsDelegate: ArcsPaneJfxDelegate =>
        arcsDelegate.arcFirstColorProperty.setArcColor = this.setArcColor(0)
        arcsDelegate.arcSecondColorProperty.setArcColor = this.setArcColor(1)
      case _ =>
    }
  }

  styleClass += "arcs-pane"
  initDelegate()
  pickOnBounds = false
}
