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
import scalafx.scene.Node
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

  private class PrefLabel(text: String) extends Label(text) {
    font = FontProvider.regularFont(FontWeight.Normal, 15)
  }

  private class PrefLineLayout(labelText: String, node: Node) extends
      HBox(30, new PrefLabel(labelText), node) {

    alignment = Pos.BaselineRight
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

  private val mCenterCircleRadiusSpinner = new Spinner[Double](50, dataController.rouletteData.wheelRadius,
      dataController.rouletteData.centerCircleRadius) {

    editable = true
    prefWidth = 80

    value.onChange(dataController.rouletteData.centerCircleRadius = value.value)
    editor.value.textProperty().addListener(new DigitSpinnerChecker(3, editor.value))
  }

  children = List(new PrefLineLayout("Count:", mChoiceCountSpinner),
    new PrefLineLayout("Radius:", mWheelRadiusSpinner),
    new PrefLineLayout("Center:", mCenterCircleRadiusSpinner))
  minWidth = 200
  spacing = 30
  alignment = Pos.TopCenter
  padding = Insets(10)
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"
}
