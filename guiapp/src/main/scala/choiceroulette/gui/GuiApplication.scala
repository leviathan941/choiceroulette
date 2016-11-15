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

package choiceroulette.gui

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.{Priority, VBox}
import scalafx.scene.paint.Color

/**
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object GuiApplication extends JFXApp {

  private val mAppMenuBar = new AppMenuBar
  private val mRoulettePane = new RoulettePane

  stage = new PrimaryStage {
    title = "Choice Roulette"
    scene = new Scene() {
      fill = Color.LightGrey
      root = new VBox(mAppMenuBar, mRoulettePane) {
        fillWidth = true
        prefWidth = 1024
        prefHeight = 768
        maxWidth = Double.MaxValue
        maxHeight = Double.MaxValue
        margin = Insets(0)
        VBox.setVgrow(mRoulettePane, Priority.Always)
      }
    }
  }
}
