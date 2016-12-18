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

import javafx.css.{CssMetaData, _}
import javafx.scene.paint.Paint

import com.sun.javafx.css.converters.PaintConverter

/** [[StyleableObjectProperty]] for setting [[ArcsPane]]'s arc colors.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcColorProperty(arcPane: ArcsPaneJfxDelegate,
                       name: String,
                       metaData: CssMetaData[ArcsPaneJfxDelegate, Paint]) extends StyleableObjectProperty[Paint] {

  var setArcColor: Paint => Unit = _

  override def invalidated(): Unit = {
    get() match {
      case paint: Paint => setArcColor(paint)
      case _ =>
    }
  }

  override def getCssMetaData: CssMetaData[_ <: Styleable, Paint] = metaData
  override def getName: String = name
  override def getBean: ArcsPaneJfxDelegate = arcPane

}

object ArcColorProperty {

  private class ArcColorMetaData(property: String,
                                 styleableProperty: ArcsPaneJfxDelegate => StyleableProperty[Paint]) extends
    CssMetaData[ArcsPaneJfxDelegate, Paint](property, PaintConverter.getInstance()) {
    override def isSettable(styleable: ArcsPaneJfxDelegate): Boolean = true

    override def getStyleableProperty(styleable: ArcsPaneJfxDelegate): StyleableProperty[Paint] =
      styleableProperty(styleable)
  }

  val firstCssMetaData: CssMetaData[ArcsPaneJfxDelegate, Paint] =
    new ArcColorMetaData("-arc-first-background-color", _.arcFirstColorProperty)

  val secondCssMetaData: CssMetaData[ArcsPaneJfxDelegate, Paint] =
    new ArcColorMetaData("-arc-second-background-color", _.arcSecondColorProperty)
}
