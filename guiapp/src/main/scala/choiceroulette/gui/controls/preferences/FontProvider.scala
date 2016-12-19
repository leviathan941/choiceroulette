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

import scalafx.scene.text.{Font, FontWeight}

/** Provides fonts for different UI elements.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object FontProvider {

  private val mDefaultFontFamily =
    if (Font.families.contains("PF DinText Pro")) "PF DinText Pro"
    else "Helvetica"

  private val mDefaultFontSize = 18

  val regularFont: Font = regularFont(FontWeight.Normal, mDefaultFontSize)

  val boldRegularFont: Font = regularFont(FontWeight.Bold, mDefaultFontSize)

  def regularFont(weight: FontWeight, size: Double): Font = Font.font(mDefaultFontFamily, weight, size)
}
