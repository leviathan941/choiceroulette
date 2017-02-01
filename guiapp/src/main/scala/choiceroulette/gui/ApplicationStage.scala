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

package choiceroulette.gui

import java.nio.file.{Files, Paths}

import choiceroulette.configuration.ConfigurationManager
import choiceroulette.gui.utils.FileUtils
import scaldi.Injector

import scalafx.Includes.handle
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.stage.Stage

/** Main stage containing application UI.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ApplicationStage(splash: Option[Splash], configManager: ConfigurationManager, protected val mainPane: MainPane)
    (implicit val injector: Injector) extends Stage {

  private val mMainScene = new Scene {
    root = mainPane
    fill = Color.Transparent
  }

  title = "Choice Roulette"
  scene = mMainScene

  onShown = handle {
    splash match {
      case Some(stage) => stage.close()
      case _ =>
    }
  }

  override def close(): Unit = {
    super.close()
  }

  private def applyLastStylesheet(): Unit = {
    configManager.getString(GuiConfigs.lastStylesheetConfigKey) match {
      case path: String if path.nonEmpty => applyStylesheet(path)
      case _ =>
    }
  }

  def applyStylesheet(filePath: String): Unit = {
    if (Files.exists(Paths.get(filePath))) {
      mMainScene.stylesheets.clear()
      mMainScene.stylesheets.add(FileUtils.filePathToUrl(filePath))
      configManager.setString(GuiConfigs.lastStylesheetConfigKey, filePath)
    }
  }

  // Apply last stylesheet on creating
  applyLastStylesheet()
}
