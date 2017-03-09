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
import enumeratum.{Enum, EnumEntry}
import net.ceedubs.ficus.readers.ArbitraryTypeReader

import scala.collection.immutable.IndexedSeq
import scalafx.beans.property.{DoubleProperty, ObjectProperty, StringProperty}

/** Roulette data holder to collect data that can be changed dynamically.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
sealed trait DataHolder

object DataHolder {

  sealed trait DataType extends EnumEntry

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
  object ArcDataHolder {
    lazy val configKeyPrefix: String = GuiConfigs.configKeyPrefix + ".arcs-config"
    lazy val labelsConfigKey: String = configKeyPrefix + ".arcLabels"

    lazy val labelDefaultText: String = "Enter choice"
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
                           private var _arcsCount: Int,
                           private var _removeWonArc: Boolean) extends DataHolder
      with ArbitraryTypeReader
      with Configurable
      with DataChangeListenable[RouletteDataHolder, RouletteDataHolder.RouletteDataType] {

    def wheelRadius: Double = _wheelRadius
    def wheelRadius_=(radius: Double): Unit = {
      if (_wheelRadius != radius) {
        _wheelRadius = radius
        notifyListeners(this, RouletteDataHolder.RouletteDataType.WheelRadius)
      }
    }

    def centerCircleRadius: Double = _centerCircleRadius
    def centerCircleRadius_=(radius: Double): Unit = {
      if (_centerCircleRadius != radius) {
        _centerCircleRadius = radius
        notifyListeners(this, RouletteDataHolder.RouletteDataType.CenterCircleRadius)
      }
    }

    def arcsCount: Int = _arcsCount
    def arcsCount_=(count: Int): Unit = {
      val limitedCount = RouletteDataHolder.limitArcsCount(count)
      if (_arcsCount != limitedCount) {
        _arcsCount = limitedCount
        notifyListeners(this, RouletteDataHolder.RouletteDataType.ArcsCount)
      }
    }

    def wonArcRemovable: Boolean = _removeWonArc
    def wonArcRemovable_=(removable: Boolean): Unit = {
      if (_removeWonArc != removable) {
        _removeWonArc = removable
        notifyListeners(this, RouletteDataHolder.RouletteDataType.RemoveWonArc)
      }
    }

    override def toConfig: Config = {
      ConfigFactory.empty().
        withValue(RouletteDataHolder.wheelRadiusConfigKey, ConfigValueFactory.fromAnyRef(wheelRadius)).
        withValue(RouletteDataHolder.centerCircleRadiusConfigKey, ConfigValueFactory.fromAnyRef(centerCircleRadius)).
        withValue(RouletteDataHolder.arcsCountConfigKey, ConfigValueFactory.fromAnyRef(arcsCount)).
        withValue(RouletteDataHolder.removeWonArcConfigKey, ConfigValueFactory.fromAnyRef(wonArcRemovable))
    }
  }
  object RouletteDataHolder {
    lazy val configKeyPrefix: String = GuiConfigs.configKeyPrefix + ".roulette-config"
    lazy val wheelRadiusConfigKey: String = configKeyPrefix + ".wheelRadius"
    lazy val centerCircleRadiusConfigKey: String = configKeyPrefix + ".centerCircleRadius"
    lazy val arcsCountConfigKey: String = configKeyPrefix + ".arcsCount"
    lazy val removeWonArcConfigKey: String = configKeyPrefix + ".removeWonArc"

    lazy val arcsCountLimits: Range = 0 to 50

    sealed trait RouletteDataType extends DataType
    object RouletteDataType extends Enum[RouletteDataType] {
      override def values: IndexedSeq[RouletteDataType] = findValues

      case object WheelRadius extends RouletteDataType
      case object CenterCircleRadius extends RouletteDataType
      case object ArcsCount extends RouletteDataType
      case object RemoveWonArc extends RouletteDataType
    }

    def limitArcsCount(count: Int): Int = {
      count match {
        case c if RouletteDataHolder.arcsCountLimits.contains(c) => count
        case c if c < RouletteDataHolder.arcsCountLimits.start => RouletteDataHolder.arcsCountLimits.start
        case c if c > RouletteDataHolder.arcsCountLimits.end => RouletteDataHolder.arcsCountLimits.end
      }
    }

    def apply(wheelRadius: Double = 250,
              centerCircleRadius: Double = 20,
              arcsCount: Int = 2,
              removeWonArc: Boolean = false): RouletteDataHolder =
      new RouletteDataHolder(wheelRadius, centerCircleRadius, limitArcsCount(arcsCount), removeWonArc)
  }

}
