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

import scalafx.scene.shape.Arc
import scalafx.scene.text.Text
import scalafx.scene.transform.Rotate

/** Text to be placed in an arc.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcText(text: String) extends Text(text) {

  def moveInsideArc(arc: Arc, point: (Double, Double)): Unit = {
    val angle = math.toRadians(rotateTextToArc(arc))
    val (dx, dy) = shiftTextCenterWidth(angle)

    relocate(point._1 + dx, point._2 - dy)
  }

  private def rotateTextToArc(arc: Arc): Double = {
    val angle = 180 - arc.startAngle.value - arc.length.value / 2
    transforms.add(new Rotate(angle, layoutBounds.value.getMinX, layoutBounds.value.getMinY))
    angle
  }

  private def shiftTextCenterWidth(rotationAngle: Double): (Double, Double) = {
    val shift = layoutBounds.value.getHeight / 2
    (shift * math.sin(rotationAngle), shift * math.cos(rotationAngle))
  }
}
