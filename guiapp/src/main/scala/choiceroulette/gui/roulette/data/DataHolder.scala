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

package choiceroulette.gui.roulette.data

import choiceroulette.gui.utils.Conversions._

import javafx.scene.paint.Paint
import javafx.scene.text.Font

import scalafx.beans.property.{DoubleProperty, ObjectProperty, StringProperty}

/** Roulette data holder to collect data that can be changed dynamically.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
trait DataHolder

object DataHolder {

  class ArcDataHolder(fillProp: ObjectProperty[Paint],
                      strokeProp: ObjectProperty[Paint],
                      strokeWidthProp: DoubleProperty) extends DataHolder {

    private var fillValue: Paint = fillProp
    private var strokeValue: Paint = strokeProp
    private var strokeWidthValue: Double = strokeWidthProp

    fillProp.onChange { fillValue = fillProp }
    strokeProp.onChange { strokeValue = strokeProp }
    strokeWidthProp.onChange { strokeWidthValue = strokeWidthProp }

    def fill: Paint = fillValue
    def fill_=(paint: Paint): Unit = {
      fillProp.value = paint
      fillValue = paint
    }

    def stroke: Paint = strokeValue
    def stroke_=(paint: Paint): Unit = {
      strokeProp.value = paint
      strokeValue = paint
    }

    def strokeWidth: Double = strokeWidthValue
    def strokeWidth_=(width: Double): Unit = {
      strokeWidthProp.value = width
      strokeWidthValue = width
    }

    def from(holder: ArcDataHolder): Unit = {
      fill = holder.fill
      stroke = holder.stroke
      strokeWidth = holder.strokeWidth
    }
  }

  class ArcLabelDataHolder(textFillProp: ObjectProperty[Paint],
                           fontProp: ObjectProperty[Font],
                           textProp: StringProperty) extends DataHolder {

    private var textFillValue: Paint = textFillProp
    private var fontValue: Font = fontProp
    private var textValue: String = textProp

    textFillProp.onChange { textFillValue = textFillProp }
    fontProp.onChange { fontValue = fontProp }
    textProp.onChange { textValue = textProp }

    def textFill: Paint = textFillValue
    def textFill_=(paint: Paint): Unit = {
      textFillProp.value = paint
      textFillValue = paint
    }

    def font: Font = fontValue
    def font_=(newFont: Font): Unit = {
      fontProp.value = newFont
      fontValue = newFont
    }

    def text: String = textValue
    def text_=(str: String): Unit = {
      textProp.value = str
      textValue = str
    }
  }

  class CursorArcDataHolder(fillProp: ObjectProperty[Paint]) extends DataHolder {

    private var fillValue: Paint = fillProp

    fillProp.onChange { fillValue = fillProp }

    def fill: Paint = fillValue
    def fill_=(paint: Paint): Unit = {
      fillProp.value = paint
      fillValue = paint
    }
  }

  class BackgroundCircleDataHolder(fillProp: ObjectProperty[Paint],
                                   strokeProp: ObjectProperty[Paint],
                                   strokeWidthProp: DoubleProperty) extends DataHolder {

    private var fillValue: Paint = fillProp
    private var strokeValue: Paint = strokeProp
    private var strokeWidthValue: Double = strokeWidthProp

    fillProp.onChange { fillValue = fillProp }
    strokeProp.onChange { strokeValue = strokeProp }
    strokeWidthProp.onChange { strokeWidthValue = strokeWidthProp }


    def fill: Paint = fillValue
    def fill_=(paint: Paint): Unit = {
      fillProp.value = paint
      fillValue = paint
    }

    def stroke: Paint = strokeValue
    def stroke_=(paint: Paint): Unit = {
      strokeProp.value = paint
      strokeValue = paint
    }

    def strokeWidth: Double = strokeWidthValue
    def strokeWidth_=(width: Double): Unit = {
      strokeWidthProp.value = width
      strokeWidthValue = width
    }
  }

  class CenterCircleDataHolder(fillProp: ObjectProperty[Paint]) extends DataHolder {

    private var fillValue: Paint = fillProp

    fillProp.onChange { fillValue = fillProp }

    def fill: Paint = fillValue
    def fill_=(paint: Paint): Unit = {
      fillProp.value = paint
      fillValue = paint
    }
  }

  case class RouletteDataHolder(fills: Array[Paint], radius: Double) extends DataHolder
}
