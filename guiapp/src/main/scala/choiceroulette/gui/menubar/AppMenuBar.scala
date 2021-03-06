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

import choiceroulette.gui.{GuiApplication, ViewType}

import scalafx.Includes.handle
import scalafx.geometry.Insets
import scalafx.scene.control.{Menu, MenuBar, MenuItem}
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}

/** Application menu bar.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class AppMenuBar(menuBarController: MenuBarController) extends MenuBar {

  private trait ResetableMenu {
    def resetItems(): Unit
  }

  private val mHelpMenu = new Menu("Help") {

    private val mAboutItem = new MenuItem("About") {
      onAction = handle(mAboutStage.show())
    }

    private lazy val mAboutStage = new AboutStage(GuiApplication.stage)

    items = List(mAboutItem)
  }

  private val mFileMenu = new Menu("File") {

    private val mApplyCss = new MenuItem("Apply Theme") {
      accelerator = new KeyCodeCombination(KeyCode.A, KeyCombination.ShortcutDown, KeyCombination.ShiftDown)
      onAction = handle(menuBarController.openCssFile())
    }

    private val mGrabFromFile = new MenuItem("Grab Values From...") {
      accelerator = new KeyCodeCombination(KeyCode.G, KeyCombination.ShortcutDown)
      onAction = handle(menuBarController.chooseFileToGrab())
    }

    private val mSaveResult = new MenuItem("Save Result To...") {
      accelerator = new KeyCodeCombination(KeyCode.S, KeyCombination.ShortcutDown)
      onAction = handle(menuBarController.chooseSaveFile())
    }

    items = List(mApplyCss, mGrabFromFile, mSaveResult)
  }

  private val mViewMenu = new Menu("View") with ResetableMenu {

    private def changeViewTitle: String = neededViewType.toString + " View"

    private def neededViewType: ViewType = {
       menuBarController.viewType match {
        case ViewType.Normal => ViewType.Compact
        case ViewType.Compact => ViewType.Normal
      }
    }

    private val mChangeView = new MenuItem(changeViewTitle) {
      accelerator = new KeyCodeCombination(KeyCode.C, KeyCombination.ShortcutDown, KeyCombination.ShiftDown)
      onAction = handle {
        menuBarController.viewType = neededViewType
        text = changeViewTitle
      }
    }

    items = List(mChangeView)

    override def resetItems(): Unit = {
      mChangeView.text = changeViewTitle
    }
  }

  private val mRunMenu = new Menu("Run") with ResetableMenu {

    private def grabbingTitle: String = {
      if (menuBarController.isGrabbingEnabled)
        "Stop Grabbing"
      else
        "Start Grabbing"
    }

    private val mGrabbing = new MenuItem(grabbingTitle) {
      accelerator = new KeyCodeCombination(KeyCode.R, KeyCombination.ShortcutDown)
      onAction = handle {
        menuBarController.setGrabbing(!menuBarController.isGrabbingEnabled)
        text = grabbingTitle
      }
    }

    items = List(mGrabbing)

    override def resetItems(): Unit = {
      mGrabbing.text = grabbingTitle
    }
  }

  def resetMenu(): Unit = {
    mRunMenu.resetItems()
    mViewMenu.resetItems()
  }

  useSystemMenuBar = true
  padding = Insets(0)
  style = "-fx-border-style: solid;" +
    "-fx-border-color: grey;" +
    "-fx-border-width: 0 1px 1px 0;"
  menus = List(mFileMenu, mViewMenu, mRunMenu, mHelpMenu)
}
