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

package choiceroulette.gui.roulette.data

import java.nio.file.Path

import choiceroulette.file.FileWatcher
import choiceroulette.gui.roulette.arc.ArcsController
import choiceroulette.gui.utils.FileUtils

import scalafx.application.Platform

/** Grabs roulette data from file and listens to further modifications.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class TextDataGrabber(arcsController: ArcsController) {

  private var mFileWatcher: Option[FileWatcher] = None
  private var mDataCache: List[String] = Nil

  def start(filePath: Path): Unit = {
    fillWheel(filePath)

    mFileWatcher match {
      case Some(watcher) => watcher.stopWatching()
      case None => mFileWatcher = Some(new FileWatcher(filePath, path => updateData(path)))
    }
    mFileWatcher.get.startWatching()
  }

  def stop(): Unit = {
    if (mFileWatcher.isDefined) {
      mFileWatcher.get.stopWatching()
      mFileWatcher = None
    }
  }

  def isRunning: Boolean = mFileWatcher.isDefined

  private def updateData(filePath: Path): Unit = {
    val data = FileUtils.fileTextLines(filePath)
    val added = data.diff(mDataCache)
    val removed = mDataCache.diff(data)

    Platform.runLater {
      val wheelData = removeFirstOccurrences(arcsController.textData, removed) ::: added
      arcsController.fillPane(wheelData)
    }
    mDataCache = data
  }

  private def removeFirstOccurrences[A](list: List[A], toRemove: List[A]): List[A] = {
    if (toRemove.nonEmpty){
      val (prefix, suffix) = list.span(_ != toRemove.head)
      removeFirstOccurrences(prefix ++ suffix.tail, toRemove.tail)
    } else {
      list
    }
  }

  private def fillWheel(filePath: Path): Unit = {
    val data = FileUtils.fileTextLines(filePath)
    Platform.runLater(arcsController.fillPane(data))
    mDataCache = data
  }
}
