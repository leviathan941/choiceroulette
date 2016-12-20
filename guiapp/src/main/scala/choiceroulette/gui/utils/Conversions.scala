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

package choiceroulette.gui.utils

import scala.language.implicitConversions
import scalafx.beans.property.DoubleProperty

/** Various usable implicit conversions.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object Conversions {
  implicit def double2DoubleProperty(number: Double): DoubleProperty = DoubleProperty(number)
  implicit def doubleProperty2Double(property: DoubleProperty): Double = property.value
}
