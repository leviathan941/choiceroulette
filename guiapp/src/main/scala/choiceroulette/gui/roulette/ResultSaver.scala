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

package choiceroulette.gui.roulette

import java.nio.charset.Charset
import java.nio.file.{Files, Path, Paths}

import choiceroulette.configuration.ConfigurationManager
import choiceroulette.gui.GuiConfigs

/** Saves spin result.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class ResultSaver(configManager: ConfigurationManager) {

  def save(result: String): Unit = {
    try {
      configManager.getString(GuiConfigs.lastSaveResultFileConfigKey) match {
        case path if !path.isEmpty => saveToFile(result, Paths.get(path))
        case _ =>
      }
    } catch {
      // TODO Write log here
      case ex: Exception => println(ex.getMessage)
    }
  }

  private def saveToFile(result: String, path: Path): Unit = {
    if (!Files.exists(path)) {
      createFile(path)
    }

    val writer = Files.newBufferedWriter(path, Charset.forName("utf-8"))
    try {
      writer.write(result)
    } finally writer.close()
  }

  private def createFile(path: Path): Unit = {
    path.getParent match {
      case dirPath: Path => Files.createDirectories(dirPath)
      case _ =>
    }
    Files.createFile(path)
  }
}
