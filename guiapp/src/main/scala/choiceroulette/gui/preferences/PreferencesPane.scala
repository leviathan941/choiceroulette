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

package choiceroulette.gui.preferences

import scalafx.Includes
import scalafx.geometry.Insets
import scalafx.scene.control.Spinner
import scalafx.scene.layout.VBox

/** Pane for roulette preferences.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class PreferencesPane(private val prefChangeListener: PreferencesChangeListener) extends VBox {

  private val mMaxCountSpinner = new Spinner[Int](1, 16, 2) {
    editable = true
    prefWidth = 100
    editor.value.setOnAction(Includes.handle {
      prefChangeListener.choiceCountChanged(value.value)
    })
  }

  children = List(mMaxCountSpinner)
  minWidth = 200
  padding = Insets(10)
  style = "-fx-border-width: 1px;" +
      "-fx-border-color: grey;"
}
