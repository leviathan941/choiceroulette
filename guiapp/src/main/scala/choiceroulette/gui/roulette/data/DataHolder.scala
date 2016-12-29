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

import javafx.scene.paint.Paint
import javafx.scene.text.Font

import choiceroulette.configuration.Configurable
import choiceroulette.gui.GuiConfigs
import choiceroulette.gui.utils.Conversions._
import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import net.ceedubs.ficus.readers.ArbitraryTypeReader

import scalafx.beans.property.{DoubleProperty, ObjectProperty, StringProperty}

/** Roulette data holder to collect data that can be changed dynamically.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
sealed trait DataHolder

object DataHolder {

  val rouletteConfigKeyPrefix: String = GuiConfigs.guiConfigKeyPrefix + ".roulette-config"
  val wheelRadiusConfigKey: String = rouletteConfigKeyPrefix + ".wheelRadius"
  val centerCircleRadiusConfigKey: String = rouletteConfigKeyPrefix + ".centerCircleRadius"
  val arcsCountConfigKey: String = rouletteConfigKeyPrefix + ".arcsCount"

  /** Holds arc's data except label.
    *
    * @see [[ArcLabelDataHolder]]
    */
  class ArcDataHolder(fillProp: ObjectProperty[Paint],
                      strokeProp: ObjectProperty[Paint],
                      strokeWidthProp: DoubleProperty,
                      val labelDataHolder: ArcLabelDataHolder) extends DataHolder {

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
      labelDataHolder.from(holder.labelDataHolder)
    }
  }

  /** Holds arc's label data.
    *
    * @see [[ArcDataHolder]]
    */
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

    def from(holder: ArcLabelDataHolder): Unit = {
      textFill = holder.textFill
      font = holder.font
      text = holder.text
    }
  }

  /** Holds arc cursor's data.
    */
  class CursorArcDataHolder(fillProp: ObjectProperty[Paint]) extends DataHolder {

    private var fillValue: Paint = fillProp

    fillProp.onChange { fillValue = fillProp }

    def fill: Paint = fillValue
    def fill_=(paint: Paint): Unit = {
      fillProp.value = paint
      fillValue = paint
    }
  }

  /** Holds background circle's data.
    */
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

  /** Holds center circle's data.
    */
  class CenterCircleDataHolder(fillProp: ObjectProperty[Paint]) extends DataHolder {

    private var fillValue: Paint = fillProp

    fillProp.onChange { fillValue = fillProp }

    def fill: Paint = fillValue
    def fill_=(paint: Paint): Unit = {
      fillProp.value = paint
      fillValue = paint
    }
  }

  /** Holds data storable in configuration file.
    */
  class RouletteDataHolder(private var _wheelRadius: Double,
                           private var _centerCircleRadius: Double,
                           private var _arcsCount: Int) extends DataHolder
      with ArbitraryTypeReader
      with Configurable
      with DataChangeListenable[RouletteDataHolder] {

    def wheelRadius: Double = _wheelRadius
    def wheelRadius_=(radius: Double): Unit = {
      _wheelRadius = radius
      notifyListeners(this)
    }

    def centerCircleRadius: Double = _centerCircleRadius
    def centerCircleRadius_=(radius: Double): Unit = {
      _centerCircleRadius = radius
      notifyListeners(this)
    }

    def arcsCount: Int = _arcsCount
    def arcsCount_=(count: Int): Unit = {
      _arcsCount = RouletteDataHolder.limitArcsCount(count)
      notifyListeners(this)
    }

    override def toConfig: Config = {
      ConfigFactory.empty().
        withValue(wheelRadiusConfigKey, ConfigValueFactory.fromAnyRef(wheelRadius)).
        withValue(centerCircleRadiusConfigKey, ConfigValueFactory.fromAnyRef(centerCircleRadius)).
        withValue(arcsCountConfigKey, ConfigValueFactory.fromAnyRef(arcsCount))
    }
  }
  object RouletteDataHolder {
    val arcsCountLimits: Range = 2 to 50

    def limitArcsCount(count: Int): Int = {
      count match {
        case c if RouletteDataHolder.arcsCountLimits.contains(c) => count
        case c if c < RouletteDataHolder.arcsCountLimits.start => RouletteDataHolder.arcsCountLimits.start
        case c if c > RouletteDataHolder.arcsCountLimits.end => RouletteDataHolder.arcsCountLimits.end
      }
    }

    def apply(wheelRadius: Double = 250, centerCircleRadius: Double = 50, arcsCount: Int = 2): RouletteDataHolder =
      new RouletteDataHolder(wheelRadius, centerCircleRadius, limitArcsCount(arcsCount))
  }

}
