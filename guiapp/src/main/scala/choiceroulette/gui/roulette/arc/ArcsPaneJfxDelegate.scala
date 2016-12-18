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

package choiceroulette.gui.roulette.arc

import java.util
import javafx.css.{CssMetaData, Styleable}
import javafx.scene.layout.StackPane

/** Delegate for [[ArcsPane]] for providing CSS meta data.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcsPaneJfxDelegate extends StackPane {

  val arcFirstColorProperty: ArcColorProperty = new ArcColorProperty(this, "arcFirstColor",
    ArcColorProperty.firstCssMetaData)

  val arcSecondColorProperty: ArcColorProperty = new ArcColorProperty(this, "arcSecondColor",
    ArcColorProperty.secondCssMetaData)

  override def getCssMetaData: util.List[CssMetaData[_ <: Styleable, _]] = {
    val styleables = new util.ArrayList[CssMetaData[_ <: Styleable, _]](super.getCssMetaData)
    styleables.add(ArcColorProperty.firstCssMetaData)
    styleables.add(ArcColorProperty.secondCssMetaData)
    styleables
  }
}
