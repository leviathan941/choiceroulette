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
import scala.util.Random

/** Util methods for various calculations in circle.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object CircleUtils {

  def shiftPointAlongRadius(center: (Double, Double),
                            p: (Double, Double),
                            shift: Double): (Double, Double) = {
    val (r, phi) = cartesianToPolar(p, center)
    polarToCartesian((r + shift, phi), center)
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

  def cartesianToPolar(cartesian: (Double, Double),
                       center: (Double, Double)): (Double, Double) = {
    val x = cartesian._1 - center._1
    val y = cartesian._2 - center._2
    (math.sqrt(x * x + y * y), math.atan2(y, x))
  }

  def polarToCartesian(polar: (Double, Double),
                       center: (Double, Double)): (Double, Double) = {
    (polar._1 * math.cos(polar._2) + center._1,
      polar._1 * math.sin(polar._2) + center._2)
  }

  def randomAngleBetween(start: Double, end: Double, offset: Double): Double = {
    offset + start + Random.nextDouble() * (end - start - offset)
  }

  def simplifyAngle(degrees: Double): Double = {
    degrees % 360
  }

  /** @param degrees Angle of degrees.
    * @return angle converted from [-180, 180] to [0, 360].
    */
  def fromMathTo0to2Pi(degrees: Double): Double = {
    if (degrees > 0) 360 - degrees
    else -degrees
  }
}
