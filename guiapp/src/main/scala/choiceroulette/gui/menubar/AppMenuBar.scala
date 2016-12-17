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

import choiceroulette.gui.GuiApplication

import scalafx.Includes.handle
import scalafx.geometry.Insets
import scalafx.scene.control.{Menu, MenuBar, MenuItem}

/** Application menu bar.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class AppMenuBar(menuBarController: MenuBarController) extends MenuBar {

  private val mHelpMenu = new Menu("Help") {

    private lazy val mAboutItem = new MenuItem("About") {
      onAction = handle(mAboutStage.show())
    }

    private lazy val mAboutStage = new AboutStage(GuiApplication.stage)

    items = List(mAboutItem)
  }

  private val mFileMenu = new Menu("File") {

    private lazy val mApplyCss = new MenuItem("Apply style from CSS") {
      onAction = handle(menuBarController.openCssFile())
    }

    items = List(mApplyCss)
  }

  useSystemMenuBar = true
  padding = Insets(0)
  style = "-fx-border-style: solid;" +
    "-fx-border-color: grey;" +
    "-fx-border-width: 0 1px 1px 0;"
  menus = List(mFileMenu, mHelpMenu)
}
