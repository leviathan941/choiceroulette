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

package choiceroulette.gui.controls.preferences

import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.scene.control.TextField

import choiceroulette.gui.roulette.data.DataHolder.RouletteDataHolder
import choiceroulette.gui.roulette.data.RouletteDataController

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.{Label, Spinner}
import scalafx.scene.layout.{HBox, VBox}
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

  private val mChoiceCountSpinner = new Spinner[Int](
      RouletteDataHolder.arcsCountLimits.start,
      RouletteDataHolder.arcsCountLimits.end,
      dataController.rouletteData.arcsCount) {

    editable = true
    prefWidth = 80

    value.onChange(dataController.rouletteData.arcsCount = value.value)
    editor.value.textProperty().addListener(new DigitSpinnerChecker(2, editor.value))
  }

  private val mWheelRadiusSpinner = new Spinner[Double](250, 500, dataController.rouletteData.wheelRadius) {
    editable = true
    prefWidth = 80

    value.onChange(dataController.rouletteData.wheelRadius = value.value)
    editor.value.textProperty().addListener(new DigitSpinnerChecker(3, editor.value))
  }

  private class PrefLabel(text: String) extends Label(text) {
    font = FontProvider.regularFont(FontWeight.Normal, 15)
  }

  private val mCountSpinnerLayout = new HBox(30, new PrefLabel("Count:"), mChoiceCountSpinner) {
    alignment = Pos.BaselineRight
  }

  private val mWheelRadiusSpinnerLayout = new HBox(30, new PrefLabel("Radius:"), mWheelRadiusSpinner) {
    alignment = Pos.BaselineRight
  }

  children = List(mCountSpinnerLayout, mWheelRadiusSpinnerLayout)
  minWidth = 200
  spacing = 30
  alignment = Pos.TopCenter
  padding = Insets(10)
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"
}
