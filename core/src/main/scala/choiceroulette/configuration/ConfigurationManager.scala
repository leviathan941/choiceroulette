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

package choiceroulette.configuration

import java.io.{File, PrintWriter}
import java.util.concurrent.ConcurrentHashMap

import choiceroulette.application.ExitListener
import com.typesafe.config._
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ValueReader

import scala.collection.JavaConverters.asJavaCollectionConverter
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/** Manages writing/reading configuration.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ConfigurationManager extends ExitListener {

  @volatile private var mConfig: Config = ConfigFactory.parseResources(
    "application.conf", ConfigParseOptions.defaults())
  @volatile private var mConfigFile: File = new File(ConfigurationManager.defaultConfigFilePath)
  private lazy val mConfigurables = ConcurrentHashMap.newKeySet[Syncable]()

  private var mWriteThread = new Thread(new SyncRunnable)

  def getDouble(key: String, default: Double = 0.0): Double = mConfig.getOrElse[Double](key, default)
  def setDouble(key: String, value: Double): Unit =
    mConfig = mConfig.withValue(key, ConfigValueFactory.fromAnyRef(value))

  def getString(key: String, default: String = ""): String = mConfig.getOrElse[String](key, default)
  def setString(key: String, value: String): Unit =
    mConfig = mConfig.withValue(key, ConfigValueFactory.fromAnyRef(value))

  def getList[T](key: String, default: List[T] = Nil)(implicit reader: ValueReader[Option[List[T]]]): List[T] =
    mConfig.getOrElse(key, default)
  def setList[T](key: String, value: List[T]): Unit =
    mConfig = mConfig.withValue(key, ConfigValueFactory.fromAnyRef(value.asJavaCollection))

  def get[T](key: String, default: T)(implicit reader: ValueReader[Option[T]]): T = mConfig.getOrElse[T](key, default)
  def set[T <: Configurable](key: String, value: T): Unit =
    mConfig = mConfig.withoutPath(key).withFallback(value.toConfig)


  def registerSyncable(syncable: Syncable): Unit = {
    mConfigurables.add(syncable)
  }

  def unregisterSyncable(syncable: Syncable): Unit = {
    mConfigurables.remove(syncable)
  }


  def setConfigFile(filePath: String): Unit = {
    try {
      val file = createConfigFileIfNeeded(filePath)
      mConfigFile = file

      mConfig = ConfigFactory.parseFile(file)
    } catch {
      // TODO Write log here
      case ex: Exception => println(ex.getMessage)
    }
  }

  def writeToDisk(): Unit = {
    mWriteThread.interrupt()
    syncAll()
    writeToFile(mConfigFile)
    startNewThread()
  }

  private def createConfigFileIfNeeded(filePath: String): File = {
    val file = new File(filePath)
    if (!file.exists()) {
      ConfigurationManager.createConfigDirectory()
      file.createNewFile()
      writeToFile(file)
    }
    file
  }

  private def writeToFile(file: File): Unit = {
    try {
      val writer = new PrintWriter(file, "utf-8")
      try {
        writer.write(mConfig.root().render(
          ConfigRenderOptions.defaults().
            setComments(false).
            setOriginComments(false).
            setJson(false))
        )
      } finally writer.close()
    } catch {
      // TODO Write log here
      case ex: Exception => println(ex.getMessage)
    }
  }

  private def syncAll(): Unit = {
    mConfigurables.forEach(_.sync(ConfigurationManager.this))
  }

  private def writeTask: Future[Unit] = Future {
    syncAll()
    writeToFile(mConfigFile)
    Thread.sleep(30000)
  }

  override def onExit(): Unit = {
    mWriteThread.interrupt()
    syncAll()
    writeToFile(mConfigFile)
  }

  private def startNewThread(): Unit = {
    mWriteThread = new Thread(new SyncRunnable)
    mWriteThread.setDaemon(true)
    mWriteThread.start()
  }

  private class SyncRunnable extends Runnable {
    private def infiniteLoop(): Future[Unit] = writeTask.map(_ => infiniteLoop())

    override def run(): Unit = {
      try {
        Await.ready(infiniteLoop(), Duration.Inf)
      } catch {
        case _: InterruptedException =>
      }
    }
  }

  setConfigFile(mConfigFile.getAbsolutePath)
  mWriteThread.start()
}

object ConfigurationManager {
  private def systemConfigDir: String =
    if (System.getProperty("os.name").toLowerCase.contains("win"))
      System.getenv("AppData")
    else
      System.getProperty("user.home")

  private val configDirectory: String =
      systemConfigDir + File.separator + ".choiceroulette" + File.separator
  val defaultConfigFilePath: String = configDirectory + "choiceroulette.state"

  def createConfigDirectory(): Unit = new File(configDirectory).mkdirs()
}
