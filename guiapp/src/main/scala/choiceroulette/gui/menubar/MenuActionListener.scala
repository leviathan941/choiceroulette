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

import choiceroulette.gui.ViewType

/** Listener for menu bar actions.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
trait MenuActionListener {
  def cssFileOpened(path: String): Unit
  def saveFileChosen(path: String): Unit
  def viewTypeChanged(viewType: ViewType): Unit
  def grabFileChosen(path: String): Unit
  def grabFromFileEnabled(): Unit
  def grabFromFileDisabled(): Unit
}
