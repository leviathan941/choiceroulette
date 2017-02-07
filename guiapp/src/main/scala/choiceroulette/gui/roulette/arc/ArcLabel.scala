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

import choiceroulette.gui.controls.preferences.FontProvider
import choiceroulette.gui.roulette.data.DataHolder.ArcLabelDataHolder
import choiceroulette.gui.roulette.data.{DataHoldable, RouletteDataController}
import choiceroulette.gui.utils.Conversions._
import com.sun.javafx.tk.{FontLoader, Toolkit}

import scalafx.beans.property.DoubleProperty
import scalafx.geometry.Insets
import scalafx.scene.control.{Label, OverrunStyle}
import scalafx.scene.paint.Color
import scalafx.scene.text.TextIncludes.jfxFont2sfxFont
import scalafx.scene.transform.Rotate

/** Text label to be placed in an arc.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcLabel(dataController: RouletteDataController,
               arcAngle: (Double, Double),
               startPoint: () => (Double, Double),
               textStr: String) extends Label(textStr) with DataHoldable { label =>

  private val fontLoader: FontLoader = Toolkit.getToolkit.getFontLoader
  private val labelRotation: DoubleProperty = DoubleProperty(0.0)

  private def moveInsideArc(point: (Double, Double), rotationDegrees: Double): Unit = {
    val rads = math.toRadians(rotationDegrees)
    val (dx, dy) = shiftTextCenterWidth(rads)
    relocate(point._1 + dx, point._2 - dy)
  }

  private def rotateTextToArc(): Double = {
    val angle = 180 - arcAngle._1 - arcAngle._2 / 2
    val pivotX = layoutBounds.value.getMinX
    val pivotY = layoutBounds.value.getMinY
    transforms.add(new Rotate(angle, pivotX, pivotY))
    labelRotation.value = angle
    angle
  }

  private def shiftTextCenterWidth(rotationAngle: Double): (Double, Double) = {
    val shift = lineHeight / 2
    (shift * math.sin(rotationAngle), shift * math.cos(rotationAngle))
  }

  private def lineHeight: Double = fontLoader.getFontMetrics(font).getLineHeight
  private def fontDescent: Double = fontLoader.getFontMetrics(font).getDescent

  private def resetLabel(): Unit = {
    padding = Insets(-fontDescent, 0, 0, 0)
    moveInsideArc(startPoint(), labelRotation)
  }

  font = FontProvider.boldRegularFont
  padding = Insets(-fontDescent, 0, 0, 0)

  moveInsideArc(startPoint(), rotateTextToArc())
  font.onChange {
    font = FontProvider.limitFontSize(font.value)
    resetLabel()
  }

  wrapText = false
  textOverrun = OverrunStyle.CenterEllipsis
  textFill = Color.White
  styleClass += "arc-label"

  override def dataHolder: ArcLabelDataHolder = new ArcLabelDataHolder(textFill, font, text)
}
