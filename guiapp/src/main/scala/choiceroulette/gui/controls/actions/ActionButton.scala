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

package choiceroulette.gui.controls.actions

import choiceroulette.gui.controls.preferences.FontProvider
import choiceroulette.gui.utils.FxUtils

import scalafx.Includes.handle
import scalafx.scene.control.Button
import scalafx.scene.paint.Color

/** Button for [[ActionsPane]].
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ActionButton(text: String,
                   normalHoverPressedColor: (Color, Color, Color),
                   action: () => Unit) extends Button(text) {

  onAction = handle(action())
  onMousePressed = handle {
    background = FxUtils.backgroundColor(normalHoverPressedColor._3)
  }
  onMouseEntered = handle {
    background = FxUtils.backgroundColor(normalHoverPressedColor._2)
  }
  onMouseExited = handle {
    background = FxUtils.backgroundColor(normalHoverPressedColor._1)
  }
  onMouseReleased = handle {
    background = FxUtils.backgroundColor(normalHoverPressedColor._1)
  }

  prefWidth = 110
  font = FontProvider.boldRegularFont
  textFill = Color.White
  background = FxUtils.backgroundColor(normalHoverPressedColor._1)
}
