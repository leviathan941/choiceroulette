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

import scala.collection.immutable.NumericRange

/** Util methods for various calculations in circle.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object CircleUtils {

  def shiftPointAlongRadius(center: (Double, Double),
                            p: (Double, Double),
                            shift: Double): (Double, Double) = {
    val x = p._1 - center._1
    val y = p._2 - center._2
    val r = math.sqrt(x * x + y * y)
    val phi = math.atan2(y, x)

    val sfr = r + shift
    (sfr * math.cos(phi) + center._1, sfr * math.sin(phi) + center._2)
  }

  def getCirclePoint(center: (Double, Double),
                     radius: Double,
                     angle: Double): (Double, Double) = {
    (center._1 + radius * math.cos(angle),
      center._2 - radius * math.sin(angle))
  }

  def splitCircleToSectors(number: Int): NumericRange[Double] = {
    val angleStep = math.floor(360.0 / number * 100000) / 100000

    Range.Double.inclusive(0, 360, angleStep)
  }
}
