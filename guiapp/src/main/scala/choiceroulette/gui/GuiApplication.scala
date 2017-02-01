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

package choiceroulette.gui
import choiceroulette.configuration.{ConfigurationManager, ConfigurationModule}
import choiceroulette.gui.menubar.{MenuActionListener, MenuBarController, MenuBarModule}
import scaldi.Injectable.inject

import scalafx.application.{JFXApp, Platform}

/** Main GUI application class.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object GuiApplication extends JFXApp {
  implicit val injector = ConfigurationModule :: new GuiModule :: MenuBarModule

  private lazy val mConfigManager = inject [ConfigurationManager]
  private var mMainStage: Option[ApplicationStage] = None
  private val mSplashStage: Splash = new Splash(() => {
    setViewType(Some(mSplashStage), inject [MenuBarController].viewType)
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

  inject [MenuBarController].listenActions(new MenuActionListener {
    override def cssFileOpened(path: String): Unit = applyStylesheet(path)

    override def saveFileChosen(path: String): Unit = setSaveResultFile(path)

    override def viewTypeChanged(viewType: ViewType.Value): Unit = setViewType(None, viewType)
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

  private def setViewType(splash: Option[Splash], viewType: ViewType.Value): Unit = {
    mainStage = viewType match {
      case ViewType.Normal => new FullStage(splash, mConfigManager)
      case ViewType.Compact => new CompactStage(splash, mConfigManager)
    }
  }
}
