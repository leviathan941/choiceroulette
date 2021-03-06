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

import choiceroulette.gui.roulette.arc.ChoiceArc
import choiceroulette.gui.roulette.data.DataHolder.ArcLabelDataHolder

import scalafx.Includes._
import scalafx.scene.control.TextField
import scalafx.scene.input.{KeyCode, KeyEvent}

/** [[TextField]] for editing [[ChoiceArc]] text.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class EditChoiceField(dataHolder: ArcLabelDataHolder,
                      onHide: () => Unit,
                      textLimit: Int) extends TextField {

  maxWidth = 200
  editable = true
  text = dataHolder.text

  text.onChange((_, oldValue, newValue) => {
    if (newValue.length >= textLimit) text = oldValue
  })

  onKeyReleased = (event: KeyEvent) => {
    event.code match {
      case KeyCode.Enter => {
        dataHolder.text = text.value
        hide()
      }
      case KeyCode.Escape => onHide()
      case _ =>
    }
  }

  focused.onChange((_, _, isFocused) => {
    if (!isFocused) hide()
  })

  private def hide(): Unit = {
    visible = false
    onHide()
  }

  delegate.selectAll()
}
