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

import scalafx.Includes._
import scalafx.animation.{PauseTransition, RotateTransition, SequentialTransition}
import scalafx.scene.Node

/** Rotates the wheel and stops at the specified angle if start position is 0 degrees.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RouletteRotator(wheel: Node) {

  private var mRotationWithDelayedFinish: Option[SequentialTransition] = None

  private class SpinInterpolator extends Interpolator {

    override def curve(t: Double): Double =
      clamp(0.34567 * math.atan(16 * t - 8) + 0.5)

    private def clamp(t: Double): Double = {
      if (t < 0) 0.0
      else if (t > 1) 1.0
      else t
    }
  }

  private class RotationWithDelayedFinish(angle: Double, finished: () => Unit) extends SequentialTransition {
    node = wheel
    children = Seq(
      new RotateTransition(10.s) {
        byAngle = angle
        interpolator = new SpinInterpolator
      },
      new PauseTransition(0.5.s))

    delay = 0.4.s
    onFinished = handle(finished())
  }

  def spinTheWheel(angle: Double, finished: () => Unit): Unit = {
    mRotationWithDelayedFinish = Some(new RotationWithDelayedFinish(angle, finished))
    mRotationWithDelayedFinish.get.playFromStart()
  }

  def stopTheWheel(): Unit = {
    if (mRotationWithDelayedFinish.isDefined)
      mRotationWithDelayedFinish.get.stop()
  }
}
