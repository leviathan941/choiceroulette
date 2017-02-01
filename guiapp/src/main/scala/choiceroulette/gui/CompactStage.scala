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

import choiceroulette.configuration.ConfigurationManager
import choiceroulette.gui.utils.FxUtils
import scaldi.Injectable._
import scaldi.Injector

import scalafx.Includes.handle
import scalafx.scene.paint.Color
import scalafx.stage.StageStyle

/** Compact application stage showing CompactMainPane.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class CompactStage(splash: Option[Splash], configManager: ConfigurationManager)
                  (override implicit val injector: Injector)
                  extends ApplicationStage(splash, configManager, inject [MainPane]('CompactMainPane)) {

  initStyle(StageStyle.Transparent)
  mainPane.background = FxUtils.backgroundColor(Color.Transparent)
  mainPane.top.value.setVisible(false)

  onCloseRequest = handle(configManager.onExit())
}
