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
import scaldi.Injectable._
import scaldi.Injector

import scalafx.Includes.handle
import scalafx.stage.StageStyle

/** Full application stage showing FullMainPane.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class FullStage(splash: Option[Splash], configManager: ConfigurationManager)
               (override implicit val injector: Injector)
               extends ApplicationStage(splash, configManager, inject [MainPane]('FullMainPane)) {

  initStyle(StageStyle.Decorated)
  minWidth = 840
  minHeight = 700
  width = configManager.getDouble(GuiConfigs.windowWidthConfigKey, minWidth)
  height = configManager.getDouble(GuiConfigs.windowHeightConfigKey, minHeight)

  width.onChange(configManager.setDouble(GuiConfigs.windowWidthConfigKey, width.value))
  height.onChange(configManager.setDouble(GuiConfigs.windowHeightConfigKey, height.value))

  onCloseRequest = handle(configManager.onExit())
}
