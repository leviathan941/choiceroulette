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

package choiceroulette.gui.roulette

import choiceroulette.gui.controls.actions.ActionController
import choiceroulette.gui.controls.preferences.PreferencesController
import scaldi.Module

/** Roulette package module.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RouletteModule extends Module {
  bind [RoulettePane] to new RoulettePane(
    prefController = inject [PreferencesController],
    actionController = inject [ActionController],
    radius = 250
  )
}
