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

package choiceroulette.gui.controls.preferences

import choiceroulette.gui.roulette.data.DataHolder.RouletteDataHolder
import choiceroulette.gui.roulette.data.RouletteDataController
import scaldi.Injectable.inject

/** Controls preferences changes.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class PreferencesController(dataController: RouletteDataController) {
  implicit val injector = PreferencesModule

  private lazy val mPrefPane = inject [PreferencesPane]

  private val updatePreferencesPane: (RouletteDataHolder, RouletteDataHolder.RouletteDataType) => Unit =
    (_, dataType) => mPrefPane.update(dataType)

  dataController.rouletteData.listen(updatePreferencesPane)

  def setPreferencesEnabled(enable: Boolean = true): Unit = mPrefPane.disable = !enable
}
