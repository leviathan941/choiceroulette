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

package choiceroulette.gui

import choiceroulette.gui.controls.preferences.FontProvider

import scalafx.Includes._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.FontWeight
import scalafx.stage.StageStyle

/** Splash screen stage.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class Splash(onFinished: () => Unit) extends PrimaryStage {

  private val mAppNameLabel = new Label("Choice Roulette") {
    alignment = Pos.BaselineLeft
    font = FontProvider.regularFont(FontWeight.Bold, 48)
  }

  private val mLoadingLabel = new Label("Loading...") {
    font = FontProvider.regularFont
  }

  initStyle(StageStyle.Undecorated)
  alwaysOnTop = true
  resizable = false
  minWidth = 300
  minHeight = 110

  scene = new Scene {
    fill = Color.Azure
    content = new VBox(mAppNameLabel, mLoadingLabel) {
      padding = Insets(10)
      spacing = 10
      alignment = Pos.TopCenter
      style = "-fx-border-color: black;" +
        "-fx-border-width: 2px;"
    }
  }

  onShown = handle(onFinished())
}
