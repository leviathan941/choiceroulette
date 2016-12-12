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

import scalafx.Includes._
import scalafx.scene.control.TextField
import scalafx.scene.input.{KeyCode, KeyEvent}

/** [[TextField]] for editing [[ChoiceArc]] text.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class EditChoiceField(val choiceArc: ChoiceArc, onHide: () => Unit) extends TextField {

  maxWidth = 200
  editable = true
  text = choiceArc.text

  onKeyReleased = (event: KeyEvent) => {
    event.code match {
      case KeyCode.Enter => {
        visible = false
        choiceArc.text = text.value
        onHide()
      }
      case KeyCode.Escape => onHide()
      case _ =>
    }
  }

  delegate.selectAll()
}
