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

import choiceroulette.gui.controls.preferences.FontProvider

import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Label
import scalafx.scene.layout._
import scalafx.scene.paint._
import scalafx.scene.text.FontWeight

/** Pane for roulette roll result.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RollResultPane(result: String, size: (Double, Double)) extends StackPane {

  private val mAnswerLabel: Label = new Label(result) {
    font = FontProvider.regularFont(FontWeight.ExtraBold, 75)
  }

  private val mGradient: Paint = RadialGradient(
    0, 0,
    0.5, 0.5,
    1,
    proportional = true,
    CycleMethod.NoCycle,
    List(Stop(1, Color.Black), Stop(0, Color.Transparent)))

  children = mAnswerLabel
  background = new Background(Array(
    new BackgroundFill(mGradient, new CornerRadii(CornerRadii.Empty), Insets(0))
  ))
  alignment = Pos.Center
  prefWidth = size._1
  prefHeight = size._2
}
