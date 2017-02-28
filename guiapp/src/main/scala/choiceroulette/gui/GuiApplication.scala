/*
 * Copyright 2016, 2017 Alexey Kuzin <amkuzink@gmail.com>
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
import java.nio.file.Paths

import choiceroulette.configuration.{ConfigurationManager, ConfigurationModule}
import choiceroulette.gui.menubar.{MenuActionListener, MenuBarController, MenuBarModule}
import choiceroulette.gui.roulette.data.{RouletteDataModule, TextDataGrabber}
import scaldi.Injectable.inject

import scalafx.application.{JFXApp, Platform}

/** Main GUI application class.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object GuiApplication extends JFXApp {
  private val mGuiModule = new GuiModule
  implicit val injector = ConfigurationModule :: mGuiModule :: MenuBarModule :: RouletteDataModule

  private lazy val mConfigManager = inject [ConfigurationManager]
  private val mMenuController = inject [MenuBarController]
  private var mMainStage: Option[ApplicationStage] = None
  private val mSplashStage: Splash = new Splash(() => {
    setViewType(Some(mSplashStage), mMenuController.viewType)
  })

  def mainStage: Option[ApplicationStage] = mMainStage
  def mainStage_=(stage: ApplicationStage): Unit = {
    val thread = new Thread(() => {
      // create config manager in background thread and show main stage
      mConfigManager
      Platform.runLater {
        doForMainStage(_.close())
        mMainStage = Some(stage)
        stage.show()
      }
    })
    thread.start()
  }

  override def stopApp(): Unit = {
    stopDataGrabbing()
    super.stopApp()
  }

  mMenuController.listenActions(new MenuActionListener {
    override def cssFileOpened(path: String): Unit = applyStylesheet(path)

    override def saveFileChosen(path: String): Unit = setSaveResultFile(path)

    override def viewTypeChanged(viewType: ViewType): Unit = setViewType(None, viewType)

    override def grabFromFileEnabled(): Unit = startDataGrabbing()

    override def grabFromFileDisabled(): Unit = stopDataGrabbing()

    override def grabFileChosen(path: String): Unit = setGrabFile(path)
  })

  private def applyStylesheet(filePath: String): Unit = {
    doForMainStage(_.applyStylesheet(filePath))
  }

  private def doForMainStage(doForStage: ApplicationStage => Unit): Unit = {
    mainStage match {
      case Some(m) => doForStage(m)
      case _ =>
    }
  }

  private def setSaveResultFile(filePath: String): Unit = {
    mConfigManager.setString(GuiConfigs.lastSaveResultFileConfigKey, filePath)
  }

  private def setViewType(splash: Option[Splash], viewType: ViewType): Unit = {
    mainStage = viewType match {
      case ViewType.Normal => new FullStage(splash, mConfigManager)
      case ViewType.Compact => new CompactStage(splash, mConfigManager)
    }
  }

  private def setGrabFile(filePath: String): Unit = {
    mConfigManager.setString(GuiConfigs.lastGrabFileConfigKey, filePath)
    if (mMenuController.isGrabbingEnabled) {
      stopDataGrabbing()
      startDataGrabbing()
    }
  }

  private def startDataGrabbing(): Unit = {
    val lastGrabFilePath = mConfigManager.getString(GuiConfigs.lastGrabFileConfigKey)
    if (lastGrabFilePath.nonEmpty) {
      inject [TextDataGrabber].start(Paths.get(lastGrabFilePath))
    } else {
      // TODO Show to user
      println("Grab file is not set")
      mMenuController.menuFeedback.grabbingChanged(enabled = false)
    }
  }

  private def stopDataGrabbing(): Unit = {
    inject [TextDataGrabber].stop()
  }
}
