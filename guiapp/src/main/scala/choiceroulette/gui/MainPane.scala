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

package choiceroulette.gui

import scalafx.geometry.Insets
import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

/** Main pane containing top-level GUI components.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class MainPane(topPane: Option[Node],
               centerPane: Node,
               rightPane: Option[Node],
               bottomPane: Option[Node]) extends BorderPane {

  def this(centerPane: Node) {
    this(None, centerPane, None, None)
  }

  center = centerPane
  top = topPane.orNull
  right = rightPane.orNull
  bottom = bottomPane.orNull

  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue
  margin = Insets(0)
}
