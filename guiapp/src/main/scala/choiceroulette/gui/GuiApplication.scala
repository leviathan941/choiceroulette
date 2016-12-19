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
import choiceroulette.gui.menubar.{MenuActionListener, MenuBarController, MenuBarModule}
import choiceroulette.gui.utils.FileUtils
import scaldi.Injectable.inject

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.paint.Color

/** Main GUI application class.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object GuiApplication extends JFXApp {
  implicit val guiAppModule = new GuiModule :: MenuBarModule

  private val mMainScene = new Scene() {
    fill = Color.LightGrey
    root = inject [MainPane]
  }

  stage = new PrimaryStage {
    title = "Choice Roulette"
    scene = mMainScene
    minWidth = 840
    minHeight = 700
  }

  inject [MenuBarController].listenActions(new MenuActionListener {
    override def cssFileOpened(path: String): Unit = {
      mMainScene.stylesheets.clear()
      mMainScene.stylesheets.add(FileUtils.filePathToUrl(path))
    }
  })
}
