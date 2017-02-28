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

import choiceroulette.configuration.{ConfigurationManager, ConfigurationModule, Syncable}
import choiceroulette.gui.{GuiApplication, GuiConfigs, ViewType}
import scaldi.Injectable.inject

import scala.collection.mutable
import scalafx.application.Platform
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter

/** Controls menu bar actions.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class MenuBarController extends Syncable {
  implicit val injector = ConfigurationModule

  private lazy val mConfigurationManager = inject [ConfigurationManager]
  private lazy val mMenuActionListeners = new mutable.HashSet[MenuActionListener]()
  val menuFeedback = new MenuFeedbackListener {
    override def grabbingChanged(enabled: Boolean): Unit =
      Platform.runLater {
        mGrabbingEnabled = enabled
        if (mMenuBar.isDefined) mMenuBar.get.resetMenu()
      }
  }

  private var mViewType: ViewType = mConfigurationManager.getEnum(GuiConfigs.viewTypeConfigKey,
    ViewType.Normal : ViewType)
  private var mGrabbingEnabled: Boolean = false
  private var mMenuBar: Option[AppMenuBar] = None

  def listenActions(listener: MenuActionListener): Unit = mMenuActionListeners += listener

  def openCssFile(): Unit = {
    new FileChooser {
      title = "Choose CSS file"
      initialDirectory = new File(".")
      extensionFilters.add(new ExtensionFilter("CSS Files", "*.css"))
    }.showOpenDialog(GuiApplication.mainStage.orNull) match {
      case file: File => notifyListeners(_.cssFileOpened(file.getAbsolutePath))
      case _ =>
    }
  }

  def chooseSaveFile(): Unit = {
    val (initDir, initFileName) = initialDirFileName(
      mConfigurationManager.getString(GuiConfigs.lastSaveResultFileConfigKey))
    new FileChooser {
      title = "Choose file for saving spin result"
      initialDirectory = initDir
      initialFileName = initFileName
      extensionFilters.add(new ExtensionFilter("TXT Files", "*.txt"))
    }.showSaveDialog(GuiApplication.mainStage.orNull) match {
      case file: File => notifyListeners(_.saveFileChosen(file.getAbsolutePath))
      case _ =>
    }
  }

  def chooseFileToGrab(): Unit = {
    val (initDir, initFileName) = initialDirFileName(
      mConfigurationManager.getString(GuiConfigs.lastGrabFileConfigKey))
    new FileChooser {
      title = "Choose file to grab from"
      initialDirectory = initDir
      initialFileName = initFileName
      extensionFilters.add(new ExtensionFilter("TXT Files", "*.txt"))
    }.showOpenDialog(GuiApplication.mainStage.orNull) match {
      case file: File => notifyListeners(_.grabFileChosen(file.getAbsolutePath))
      case _ =>
    }
  }

  def viewType_=(viewType: ViewType): Unit = {
    mViewType = viewType
    notifyListeners(_.viewTypeChanged(viewType))
  }
  def viewType: ViewType = mViewType

  def setGrabbing(enabled: Boolean): Unit = {
    if (!mGrabbingEnabled)
      notifyListeners(_.grabFromFileEnabled())
    else
      notifyListeners(_.grabFromFileDisabled())

    mGrabbingEnabled = enabled
  }
  def isGrabbingEnabled: Boolean = mGrabbingEnabled

  private[menubar] def menuBar_=(menu: AppMenuBar): Unit = {
    mMenuBar = Some(menu)
  }

  private def initialDirFileName(filePath: String): (File, String) = {
    val initialPath = Paths.get(filePath)
    val initDir = initialPath.getParent match {
      case dir: Path => dir.toFile
      case _ => new File(".")
    }
    val initFileName = initialPath.getFileName match {
      case name: Path if name.toString.nonEmpty => name.toString
      case _ => "result.txt"
    }

    initDir -> initFileName
  }

  private def notifyListeners(notifyMethod: MenuActionListener => Unit): Unit = {
    mMenuActionListeners.foreach(notifyMethod)
  }

  override def sync(configurationManager: ConfigurationManager): Unit = {
    configurationManager.setEnum(GuiConfigs.viewTypeConfigKey, viewType)
  }

  inject [ConfigurationManager].registerSyncable(this)
}
