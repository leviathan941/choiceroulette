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

package choiceroulette.file

import java.nio.file._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/** Watches for a file's modifications.
  * If the file is removed, then watching is automatically stopped.
  * After `stopWatching` watcher is invalid and cannot be used anymore.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class FileWatcher(path: Path, onModified: Path => Unit) {
  require(Files.isRegularFile(path) && Files.isReadable(path))

  private val mWatchFilename: String = path.getFileName.toString
  private val mWatchService: WatchService = path.getParent.getFileSystem.newWatchService()
  private var mWatchThread: Thread = _

  def startWatching(): Unit = {
    mWatchThread = new Thread(new GrabRunnable)
    mWatchThread.setDaemon(true)
    mWatchThread.start()
  }

  def stopWatching(): Unit = {
    mWatchService.close()
    mWatchThread.interrupt()
  }

  private def watchTask: Future[Unit] = Future {
    val watchKey = mWatchService.take()
    watchKey.pollEvents().asScala.foreach(e => handleWatchEvent(e))

    if (!watchKey.reset()) {
      watchKey.cancel()
    }
  }

  private def handleWatchEvent(event: WatchEvent[_]): Unit = {
    val kind = event.kind()
    val eventPath = event.context().asInstanceOf[Path].toString

    if (eventPath == mWatchFilename) {
      kind match {
        case StandardWatchEventKinds.ENTRY_MODIFY => onModified(path)
        case StandardWatchEventKinds.ENTRY_DELETE => stopWatching()
        case k => println(k.toString)
      }
    }
  }

  private class GrabRunnable extends Runnable {
    private def infiniteLoop(): Future[Unit] = watchTask.map(_ => infiniteLoop())

    override def run(): Unit = {
      try {
        Await.ready(infiniteLoop(), Duration.Inf)
      } catch {
        case e: Exception => println(e)
      }
    }
  }

  path.getParent.register(mWatchService, StandardWatchEventKinds.ENTRY_MODIFY,
    StandardWatchEventKinds.ENTRY_DELETE)
}
