/*
 * Copyright 2017 Alexey Kuzin <amkuzink@gmail.com>
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

import scalafx.scene.paint.Color
import scalafx.scene.shape.{Arc, ArcType}

/** Arc for shading the wheel except the passed angle.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ShadingArc(radius: Double, highlightAngle: (Double, Double)) extends Arc {
  `type` = ArcType.Round
  radiusX = radius
  radiusY = radius
  startAngle = highlightAngle._2
  length = 360 - highlightAngle._2 + highlightAngle._1
  centerX = radius
  centerY = radius

  fill = Color.color(0, 0, 0, 0.65)
  styleClass += "arc-shading"
}
