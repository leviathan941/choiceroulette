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

import java.io.File
import java.nio.file.{Path, Paths}

import choiceroulette.configuration.{ConfigurationManager, ConfigurationModule}
import choiceroulette.gui.{GuiApplication, GuiConfigs}
import scaldi.Injectable.inject

import scala.collection.mutable
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter

/** Controls menu bar actions.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class MenuBarController {
  implicit val injector = ConfigurationModule

  private lazy val mMenuActionListeners = new mutable.HashSet[MenuActionListener]()

  def listenActions(listener: MenuActionListener): Unit = mMenuActionListeners += listener

  def openCssFile(): Unit = {
    new FileChooser {
      title = "Choose CSS file"
      initialDirectory = new File(".")
      extensionFilters.add(new ExtensionFilter("CSS Files", "*.css"))
    }.showOpenDialog(GuiApplication.mainStage) match {
      case file: File => notifyListeners(_.cssFileOpened(file.getAbsolutePath))
      case _ =>
    }
  }

  def chooseSaveFile(): Unit = {
    val initialPath = Paths.get(inject [ConfigurationManager].getString(GuiConfigs.lastSaveResultFileConfigKey))
    val initDir = initialPath.getParent match {
      case dir: Path => dir.toFile
      case _ => new File(".")
    }
    val initFileName = initialPath.getFileName match {
      case name: Path if name.toString.nonEmpty => name.toString
      case _ => "result.txt"
    }

    new FileChooser {
      title = "Choose file for saving spin result"
      initialDirectory = initDir
      initialFileName = initFileName
      extensionFilters.add(new ExtensionFilter("TXT Files", "*.txt"))
    }.showSaveDialog(GuiApplication.mainStage) match {
      case file: File => notifyListeners(_.saveFileChosen(file.getAbsolutePath))
      case _ =>
    }
  }

  private def notifyListeners(notifyMethod: MenuActionListener => Unit): Unit = {
    mMenuActionListeners.foreach(notifyMethod)
  }
}
