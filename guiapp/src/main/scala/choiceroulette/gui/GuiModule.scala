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

import choiceroulette.gui.controls.actions.{ActionModule, ActionsPane}
import choiceroulette.gui.controls.preferences.{PreferencesModule, PreferencesPane}
import choiceroulette.gui.menubar.{AppMenuBar, MenuBarModule}
import choiceroulette.gui.roulette.{RouletteModule, RoulettePane}
import scaldi.{Module, MutableInjectorAggregation}

/** Main module.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class GuiModule extends Module {
  override implicit val injector: MutableInjectorAggregation =
    PreferencesModule :: ActionModule :: RouletteModule :: MenuBarModule

  bind [MainPane] identifiedBy required('FullMainPane) toProvider new MainPane(
    topPane = inject [AppMenuBar],
    centerPane = inject [RoulettePane],
    rightPane = inject [PreferencesPane],
    bottomPane = inject [ActionsPane]
  )

  bind [MainPane] identifiedBy required('CompactMainPane) toProvider new MainPane(
    topPane = inject [AppMenuBar],
    centerPane = inject [RoulettePane]
  )
}
