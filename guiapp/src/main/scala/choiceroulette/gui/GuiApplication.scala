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
import java.io.File

import choiceroulette.configuration.{ConfigurationManager, ConfigurationModule}
import choiceroulette.gui.menubar.{MenuActionListener, MenuBarController, MenuBarModule}
import choiceroulette.gui.utils.FileUtils
import scaldi.Injectable.inject

import scalafx.Includes._
import scalafx.application.{JFXApp, Platform}
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.stage.{Stage, StageStyle}

/** Main GUI application class.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object GuiApplication extends JFXApp {
  implicit val guiAppModule = new GuiModule :: MenuBarModule :: ConfigurationModule

  private lazy val mConfigMgr = inject [ConfigurationManager]

  private val mSplashStage = new Splash(() => showMainPane())

  private lazy val mMainStage: Stage = new Stage {
    private val mMainScene = new Scene {
      fill = Color.LightGrey
      root = inject [MainPane]
    }

    initStyle(StageStyle.Decorated)
    title = "Choice Roulette"
    minWidth = 840
    minHeight = 700
    width = mConfigMgr.getDouble(GuiConfigs.windowWidthConfigKey, minWidth)
    height = mConfigMgr.getDouble(GuiConfigs.windowHeightConfigKey, minHeight)
    scene = mMainScene

    onCloseRequest = handle {
      mConfigMgr.setDouble(GuiConfigs.windowWidthConfigKey, width.value)
      mConfigMgr.setDouble(GuiConfigs.windowHeightConfigKey, height.value)
      mConfigMgr.onExit()
    }

    onShown = handle(
      mSplashStage.close()
    )

    //noinspection ConvertExpressionToSAM
    inject [MenuBarController].listenActions(new MenuActionListener {
      override def cssFileOpened(path: String): Unit = applyStylesheet(path)
    })

    // Apply last stylesheet on creating
    applyLastStylesheet()

    private def applyLastStylesheet(): Unit = {
      val path = mConfigMgr.getString(GuiConfigs.lastStylesheetConfigKey)
      applyStylesheet(path)
    }

    private def applyStylesheet(filePath: String): Unit = {
      if (new File(filePath).exists()) {
        mMainScene.stylesheets.clear()
        mMainScene.stylesheets.add(FileUtils.filePathToUrl(filePath))
        mConfigMgr.setString(GuiConfigs.lastStylesheetConfigKey, filePath)
      }
    }
  }

  private def showMainPane(): Unit = {
    val thread = new Thread(() => {
      // create config manager in background thread and show main stage
      mConfigMgr
      Platform.runLater(mMainStage.show())
    })
    thread.start()
  }
}
