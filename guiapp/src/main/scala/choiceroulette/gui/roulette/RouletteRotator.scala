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

import javafx.animation.Interpolator

import scalafx.Includes.handle
import scalafx.animation.RotateTransition
import scalafx.scene.Node
import scalafx.util.Duration

/** Rotates the wheel and stops at the specified angle if start position is 0 degrees.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RouletteRotator(wheel: Node, angle: Double, finished: () => Unit) {

  private class SpinInterpolator extends Interpolator {

    override def curve(t: Double): Double = {
      clamp(if (t < 0.2) 3.125 * t * t
            // TODO Slow down the end. Maybe some variation of -k(x - 1)^2 + 1 ???
            else if (t > 0.8) -3.125 * t * t + 6.25 * t - 2.125
            else 1.25 * t - 0.125
      )
    }

    private def clamp(t: Double): Double = {
      if (t < 0) 0.0
      else if (t > 1) 1.0
      else t
    }
  }

  private lazy val mRotation: RotateTransition = new RotateTransition(Duration(4000)) {
    byAngle = angle
    node = wheel
    onFinished = handle(finished())
    interpolator = new SpinInterpolator
  }

  def spinTheWheel(): Unit = {
    mRotation.playFromStart()
  }

  def stopTheWheel(): Unit = {
    mRotation.stop()
  }
}
