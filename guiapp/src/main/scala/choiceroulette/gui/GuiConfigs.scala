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

/** Configuration keys for GUI.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object GuiConfigs {
  lazy val configKeyPrefix: String = "gui-config"
  lazy val windowWidthConfigKey: String = configKeyPrefix + ".windowWidth"
  lazy val windowHeightConfigKey: String = configKeyPrefix + ".windowHeight"
  lazy val lastStylesheetConfigKey: String = configKeyPrefix + ".lastStylesheet"
  lazy val lastSaveResultFileConfigKey: String = configKeyPrefix + ".lastSaveResultFile"
  lazy val viewTypeConfigKey: String = configKeyPrefix + ".viewType"
  lazy val lastGrabFileConfigKey: String = configKeyPrefix + ".lastGrabFile"
}
