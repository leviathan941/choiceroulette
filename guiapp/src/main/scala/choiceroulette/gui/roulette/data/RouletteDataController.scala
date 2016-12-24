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

import choiceroulette.gui.roulette.data.DataHolder._

import scala.collection.mutable.ListBuffer
import scalafx.scene.paint.Color

/** Controls all roulette data.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RouletteDataController {

  private lazy val mArcsData = new ListBuffer[ArcDataHolder]

  private lazy val mArcLabelData = new ListBuffer[ArcLabelDataHolder]

  var cursorArcData: Option[CursorArcDataHolder] = None

  var backgroundCircleData: Option[BackgroundCircleDataHolder] = None

  var centerCircleData: Option[CenterCircleDataHolder] = None

  val rouletteData: RouletteDataHolder =
    RouletteDataHolder(Array(Color.Lavender, Color.Aquamarine), 250)


  def addArcData(data: ArcDataHolder): Unit = mArcsData += data
  def removeArcData(data: ArcDataHolder): Unit = mArcsData -= data

  def addArcLabelData(data: ArcLabelDataHolder): Unit = mArcLabelData += data
  def removeArcLabelData(data: ArcLabelDataHolder): Unit = mArcLabelData -= data
}
