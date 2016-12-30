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

import javafx.scene.paint.Paint

import choiceroulette.configuration.{ConfigurationManager, Syncable}
import choiceroulette.gui.roulette.data.DataHolder._
import net.ceedubs.ficus.readers.AllValueReaderInstances._

import scala.collection.mutable.ListBuffer
import scalafx.scene.paint.Color

/** Controls all roulette data.
  *
  * @author Alexey Kuzin <amkuzink@gmail.com>
  */
class RouletteDataController(configMgr: ConfigurationManager) extends Syncable {

  private lazy val mArcsData = new ListBuffer[ArcDataHolder]

  var cursorArcData: Option[CursorArcDataHolder] = None

  var backgroundCircleData: Option[BackgroundCircleDataHolder] = None

  var centerCircleData: Option[CenterCircleDataHolder] = None

  val rouletteData: RouletteDataHolder = restoreRouletteData()

  val arcFills: Array[Paint] = Array(Color.Lavender, Color.Aquamarine)


  def addArcData(data: ArcDataHolder): Unit = mArcsData += data
  def removeArcData(data: ArcDataHolder): Unit = mArcsData -= data

  override def sync(configurationManager: ConfigurationManager): Unit = {
    configurationManager.set[RouletteDataHolder](
      RouletteDataHolder.configKeyPrefix,
      rouletteData)

    configurationManager.setList(ArcDataHolder.labelsConfigKey, mArcsData.map(_.labelDataHolder.text).toList)
  }

  private def restoreRouletteData(): RouletteDataHolder = {
    configMgr.get(RouletteDataHolder.configKeyPrefix, RouletteDataHolder())
  }

  def restoreArcsData(): Unit = {
    val labelTexts = configMgr.getList(ArcDataHolder.labelsConfigKey,
      List(ArcDataHolder.labelDefaultText, ArcDataHolder.labelDefaultText))

    mArcsData.zip(labelTexts).foreach(pair => pair._1.labelDataHolder.text = pair._2)
  }

  configMgr.registerSyncable(this)
}
