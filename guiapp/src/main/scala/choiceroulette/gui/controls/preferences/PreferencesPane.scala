/*
 * Copyright 2016, 2017 Alexey Kuzin <amkuzink@gmail.com>
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

package choiceroulette.gui.controls.preferences

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.scene.control.TextField

import choiceroulette.gui.roulette.data.DataHolder.RouletteDataHolder
import choiceroulette.gui.roulette.data.DataHolder.RouletteDataHolder.RouletteDataType
import choiceroulette.gui.roulette.data.RouletteDataController

import scalafx.Includes._
import scalafx.geometry.{Insets, Orientation, Pos}
import scalafx.scene.Node
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.text.FontWeight

/** Pane for roulette preferences.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class PreferencesPane(dataController: RouletteDataController) extends VBox {

  private class DigitSpinnerChecker(maxLength: Int, inputControl: TextField) extends ChangeListener[String] {
    override def changed(observable: ObservableValue[_ <: String], oldValue: String, newValue: String): Unit = {
        if (!newValue.forall(_.isDigit) || newValue.length > maxLength) {
          inputControl.setText(oldValue)
        }
    }
  }

  private class PrefLabel(text: String) extends Label(text) {
    font = FontProvider.regularFont(FontWeight.Normal, 15)
    textFill = Color.White
    wrapText = false
    textOverrun = OverrunStyle.CenterEllipsis
    styleClass += "preference-label"

    font.onChange((_, _, newValue) => {
      font = FontProvider.limitFontSize(newValue, 15)
    })
  }

  private class PrefLineLayout(labelText: String, node: Node, spacing: Double) extends
      HBox(spacing, new PrefLabel(labelText), node) {

    alignment = Pos.CenterLeft
  }


  private val mChoiceCountSpinner = new Spinner[Int](
      RouletteDataHolder.arcsCountLimits.start,
      RouletteDataHolder.arcsCountLimits.end,
      dataController.rouletteData.arcsCount) {

    editable = true
    prefWidth = 80

    value.onChange((_, _, newValue) => {
      if (dataController.rouletteData.arcsCount != newValue)
        dataController.rouletteData.arcsCount = newValue
    })
    editor.value.textProperty().addListener(new DigitSpinnerChecker(2, editor.value))
  }

  private val mWheelRadiusSlider = new Slider(250, 500, dataController.rouletteData.wheelRadius) {
    prefWidth = 100
    orientation = Orientation.Horizontal

    value.onChange((_, _, newValue) => {
      if (dataController.rouletteData.wheelRadius != newValue.doubleValue())
        dataController.rouletteData.wheelRadius = newValue.doubleValue()
    })
  }

  private val mCenterCircleRadiusSlider = new Slider(20, 250, dataController.rouletteData.centerCircleRadius) {
    prefWidth = 100
    orientation = Orientation.Horizontal

    value.onChange((_, _, newValue) => {
      if (dataController.rouletteData.centerCircleRadius != newValue.doubleValue())
        dataController.rouletteData.centerCircleRadius = newValue.doubleValue()
    })
  }

  private val mRemoveWonArcCheckbox = new CheckBox {
    selected = dataController.rouletteData.wonArcRemovable
    indeterminate = false

    selected.onChange((_, _, newValue) => {
      if (dataController.rouletteData.wonArcRemovable != newValue)
        dataController.rouletteData.wonArcRemovable = newValue
    })
  }

  def update(dataType: RouletteDataType): Unit = {
    dataType match {
      case RouletteDataType.ArcsCount =>
        mChoiceCountSpinner.valueFactory().setValue(dataController.rouletteData.arcsCount)
      case RouletteDataType.WheelRadius =>
        mWheelRadiusSlider.value = dataController.rouletteData.wheelRadius
      case RouletteDataType.CenterCircleRadius =>
        mCenterCircleRadiusSlider.value = dataController.rouletteData.centerCircleRadius
      case RouletteDataType.RemoveWonArc =>
        mRemoveWonArcCheckbox.selected = dataController.rouletteData.wonArcRemovable
    }
  }

  children = List(new PrefLineLayout("Count:", mChoiceCountSpinner, 50),
    new PrefLineLayout("Radius:", mWheelRadiusSlider, 25),
    new PrefLineLayout("Center:", mCenterCircleRadiusSlider, 25),
    new PrefLineLayout("Remove Won Arc:", mRemoveWonArcCheckbox, 28))
  minWidth = 200
  maxWidth = 200
  spacing = 30
  alignment = Pos.TopCenter
  padding = Insets(10)
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"
}
