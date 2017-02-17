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

package choiceroulette.gui.utils

import java.net.URLEncoder
import java.nio.file.Path

import scala.io.Source

/** Utility methods for working with files.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
object FileUtils {
  val defaultEncoding: String = "utf-8"

  def filePathToUrl(path: String): String = {
    "file:///" + URLEncoder.encode(path, defaultEncoding).replace("+", "%20")
  }

  def fileTextLines(path: Path): List[String] = {
    Control.using(Source.fromFile(path.toString, defaultEncoding)) { source => {
      for (line <- source.getLines().toList
           if line.nonEmpty
      ) yield line
    }}
  }
}
