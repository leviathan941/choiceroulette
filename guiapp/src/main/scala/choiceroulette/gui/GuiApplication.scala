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
import choiceroulette.gui.menubar.MenuBarModule
import scaldi.Injectable.inject

import scalafx.application.{JFXApp, Platform}
import scalafx.stage.Stage

/** Main GUI application class.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object GuiApplication extends JFXApp {
  implicit val injector = ConfigurationModule :: new GuiModule :: MenuBarModule

  private lazy val mConfigManager = inject [ConfigurationManager]

  private var mMainStage: Option[Stage] = None

  private val mSplashStage: Splash = new Splash(() => {
    mainStage = new FullStage(Some(mSplashStage), mConfigManager)
  })

  def mainStage: Option[Stage] = mMainStage
  def mainStage_=(stage: Stage): Unit = {
    val thread = new Thread(() => {
      // create config manager in background thread and show main stage
      mConfigManager
      Platform.runLater {
        mMainStage = Some(stage)
        stage.show()
      }
    })
    thread.start()
  }

}
