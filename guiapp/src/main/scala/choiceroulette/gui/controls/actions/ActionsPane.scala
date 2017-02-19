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

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout._
import scalafx.scene.paint.Color

/** Pane for UI action controls.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ActionsPane(actionController: ActionController) extends HBox {

  private val mSpinButton = new ActionButton(
    "SPIN",
    (Color.web("#44992a"), Color.web("#4dad30"), Color.web("#32701f")),
    () => actionController.spinRoulette())

  private val mRefreshButton = new ActionButton(
    "REFRESH",
    (Color.web("00c1c1"), Color.web("00cece"), Color.web("008787")),
    () => actionController.refreshArcs()
  )

  class ActionButtonStack extends StackPane {
    children = List(mSpinButton, mRefreshButton)

    def showSpin(): Unit = {
      mRefreshButton.visible = false
      mSpinButton.visible = true
    }

    def showRefresh(): Unit = {
      mSpinButton.visible = false
      mRefreshButton.visible = true
    }

    def action: ActionController.ActionType.Value = {
      if (mSpinButton.visible.value) ActionController.ActionType.Spin
      else ActionController.ActionType.Refresh
    }
  }

  val actionButtonStack: ActionButtonStack = new ActionButtonStack
  actionButtonStack.showSpin()

  children = actionButtonStack
  alignment = Pos.BottomRight
  hgrow = Priority.Always
  padding = Insets(10, 45, 10, 45)
  style = "-fx-border-width: 1px;" +
    "-fx-border-color: grey;"
}
