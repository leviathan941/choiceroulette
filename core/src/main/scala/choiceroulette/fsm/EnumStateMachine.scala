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

package choiceroulette.fsm

import choiceroulette.fsm.EnumStateMachine.Transition
import enumeratum.EnumEntry

/** Enumeration-based finite state machine.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class EnumStateMachine[S <: EnumEntry, E <: EnumEntry]
    (transitions: Map[(S, E), Transition[S]], // (fromState, event) -> Transition
     startState: S) {
  private var mState: S = startState

  def doTransition(event: E): Unit = {
    val transition = transitions.get(mState -> event)
    if (transition.isDefined && transition.get.canBeDone) {
      mState = transition.get.toState
      transition.get.actionAfter()
    }
  }
}

object EnumStateMachine {

  sealed trait Transition[S <: EnumEntry] {
    def toState: S
    def canBeDone: Boolean
    def actionAfter(): Unit
  }

  case class DetailedTransition[S <: EnumEntry]
      (override val toState: S,
       condition: () => Boolean,
       action: () => Boolean) extends Transition[S] {
    override def canBeDone: Boolean = condition()
    override def actionAfter(): Unit = action()
  }
}
