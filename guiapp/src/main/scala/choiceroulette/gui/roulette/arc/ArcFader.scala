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

import scalafx.animation.FadeTransition
import scalafx.scene.Node
import scalafx.Includes._

/** Disappearing arc animation.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ArcFader(node: Node, finished: () => Unit) {
  private lazy val mTransition: FadeTransition = new FadeTransition(0.8.s, node) {
    fromValue = 1.0
    toValue = 0.0
    onFinished = finished
  }

  def fade(): Unit = {
    mTransition.playFromStart()
  }
}
