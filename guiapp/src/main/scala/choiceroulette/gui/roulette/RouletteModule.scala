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

package choiceroulette.gui.roulette

import choiceroulette.gui.controls.actions.ActionModule
import choiceroulette.gui.controls.preferences.PreferencesModule
import choiceroulette.gui.roulette.arc.ArcModule
import choiceroulette.gui.roulette.data.RouletteDataModule
import scaldi.{Module, MutableInjectorAggregation}

/** Roulette package module.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object RouletteModule extends Module {
  override implicit val injector: MutableInjectorAggregation =
    PreferencesModule :: ActionModule :: RouletteDataModule :: ArcModule

  binding to injected [RoulettePane]
}
