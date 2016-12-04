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

import choiceroulette.gui.utils.CircleUtils

import scalafx.scene.layout.StackPane

/** Stack pane containing choice arcs.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcsPane(private val radius: Double,
               arcsNumber: Int) extends StackPane {

  private def createRouletteSectors(number: Int): List[ChoiceArc] = {
    val angles = CircleUtils.splitCircleToSectors(number)
    for (idx <- angles.indices.toList
         if idx != 0 && idx < angles.size;
         startAngle = angles(idx - 1);
         angleLen = angles(idx) - startAngle
    ) yield new ChoiceArc(radius, startAngle, angleLen, "Choice " + idx)
  }

  def updateArcs(number: Int): Unit = {
    children = createRouletteSectors(number)
  }

  children = createRouletteSectors(arcsNumber)
}
