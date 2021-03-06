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

package choiceroulette.gui.controls.actions

import enumeratum.{Enum, EnumEntry}
import scaldi.Injectable.inject

import scala.collection.immutable.IndexedSeq
import scala.collection.mutable

/** Controls UI actions.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ActionController {
  implicit val injector = ActionModule

  private lazy val mActionListeners = new mutable.HashSet[ActionListener]()
  private lazy val mActionsPane: ActionsPane = inject [ActionsPane]

  def listenActions(listener: ActionListener): Unit = mActionListeners += listener

  def spinRoulette(): Unit = notifyListeners(_.onSpinAction())
  def refreshArcs(): Unit = notifyListeners(_.onRefreshAction())

  def actionButton_=(btnType: ActionController.ActionType): Unit = {
    btnType match {
      case ActionController.ActionType.Spin => mActionsPane.actionButtonStack.showSpin()
      case ActionController.ActionType.Refresh => mActionsPane.actionButtonStack.showRefresh()
    }
  }
  def actionButton: ActionController.ActionType = mActionsPane.actionButtonStack.action

  private def notifyListeners(notifyMethod: ActionListener => Unit): Unit = {
    mActionListeners.foreach(notifyMethod)
  }

  def setActionsEnabled(enable: Boolean = true): Unit = mActionsPane.disable = !enable
}

object ActionController {

  sealed trait ActionType extends EnumEntry
  object ActionType extends Enum[ActionType] {
    override val values: IndexedSeq[ActionType] = findValues

    case object Spin extends ActionType
    case object Refresh extends ActionType
  }
}
