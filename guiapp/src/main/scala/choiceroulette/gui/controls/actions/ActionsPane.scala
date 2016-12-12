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

package choiceroulette.gui.controls.actions

import choiceroulette.gui.controls.preferences.FontProvider

import scalafx.Includes.handle
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Button
import scalafx.scene.layout._
import scalafx.scene.paint.Color

/** Pane for UI action controls.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ActionsPane(actionController: ActionController) extends VBox {

  private val mRollButton = new Button("ROLL") {

    private lazy val mColor: Color = Color.web("#44992a")
    private lazy val mHoverColor = Color.web("#4dad30")
    private lazy val mPressedColor: Color = Color.web("#32701f")

    private def backgroundColor(color: Color) = new Background(Array(
      new BackgroundFill(color, new CornerRadii(CornerRadii.Empty), Insets(0))))

    onAction = handle(actionController.rollRoulette())
    onMousePressed = handle {
      background = backgroundColor(mPressedColor)
    }
    onMouseEntered = handle {
      background = backgroundColor(mHoverColor)
    }
    onMouseExited = handle {
      background = backgroundColor(mColor)
    }
    onMouseReleased = handle {
      background = backgroundColor(mColor)
    }

    prefWidth = 100
    font = FontProvider.boldRegularFont
    textFill = Color.White
    background = backgroundColor(mColor)
  }

  children = mRollButton
  alignment = Pos.BottomRight
  hgrow = Priority.Always
  padding = Insets(10, 50, 10, 50)
}
