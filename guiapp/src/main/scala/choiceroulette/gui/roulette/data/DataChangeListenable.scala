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

package choiceroulette.gui.roulette.data

import scala.collection.mutable
import scala.ref.WeakReference

/** Makes data holders listenable.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
trait DataChangeListenable[T <: DataHolder] {
  private lazy val mListeners = new mutable.HashSet[WeakReference[T => Unit]]()

  def listen(listener: T => Unit): Unit = {
    removeObsolete()
    ignore(listener)

    mListeners += WeakReference(listener)
  }

  def ignore(listener: T => Unit): Unit = {
    mListeners.foreach(weak => weak.get match {
      case Some(stored) if stored == listener => mListeners -= weak
      case _ =>
    })
  }

  private def removeObsolete(): Unit = {
    mListeners.foreach(weak => weak.get match {
      case None => mListeners -= weak
      case _ =>
    })
  }

  protected def notifyListeners(dataHolder: T): Unit =
    mListeners.foreach(_.get match {
      case Some(changed) => changed(dataHolder)
      case None =>
    })
}
