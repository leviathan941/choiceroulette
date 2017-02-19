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

package choiceroulette.gui.roulette.arc

import choiceroulette.gui.controls.actions.{ActionController, ActionModule}
import choiceroulette.gui.roulette.data.{RouletteDataController, RouletteDataModule}
import scaldi.{Module, MutableInjectorAggregation}

/** Module providing arcs management entities.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object ArcModule extends Module {
  override implicit val injector: MutableInjectorAggregation = RouletteDataModule :: ActionModule

  bind [ArcsController] to new ArcsController(
    inject [RouletteDataController],
    inject [ActionController]
  )(this)
  bind [ArcsPane] to new ArcsPane(inject [RouletteDataController])(this)
}
