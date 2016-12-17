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

package choiceroulette.gui.menubar

import choiceroulette.gui.GuiApplication
import scaldi.Module

/** Menu bar package module.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object MenuBarModule extends Module {

  binding toProvider new AboutStage(GuiApplication.stage)
  binding to injected [AppMenuBar]
  binding to new MenuBarController
}