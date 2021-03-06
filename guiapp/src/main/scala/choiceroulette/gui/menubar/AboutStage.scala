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

package choiceroulette.gui.menubar

import choiceroulette.gui.controls.preferences.FontProvider

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Label, TextArea}
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.text.FontWeight
import scalafx.stage.{Stage, StageStyle, Window}

/** About stage contains information about creators.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class AboutStage(owner: Window) extends Stage { about =>
  private val mAppNameLabel = new Label("Choice Roulette") {
    alignment = Pos.BaselineLeft
    padding = Insets(10)
    font = FontProvider.regularFont(FontWeight.Bold, 24)
  }

  private val mCreatorsTextArea = new TextArea {
    editable = false
    maxWidth = 340
    maxHeight = 165
    text = "Copyright (C) 2016. All rights reserved.\n" +
      "Licensed under Apache 2.0.\n\n" +
      "Creator: Alexey Kuzin (amkuzink@gmail.com)"
  }

  initOwner(owner)
  initStyle(StageStyle.Undecorated)
  title = "About Choice Roulette"
  resizable = false
  minWidth = 340
  minHeight = 215
  scene = new Scene {
    fill = Color.Azure
      content = new VBox {
        alignmentInParent = Pos.TopLeft
        style = "-fx-border-color: black;" +
              "-fx-border-width: 2px;"
        children = List(mAppNameLabel, mCreatorsTextArea)
      }
  }
  focused.onChange((_, _, newValue) => {
    if (!newValue) about.close()
  })
}
