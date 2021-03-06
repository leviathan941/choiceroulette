/*
 * Copyright 2016, 2017 Alexey Kuzin <amkuzink@gmail.com>
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

import sbt._
import Keys._

object Dependencies {
  lazy val scala212 = "2.12.1"

  lazy val scalDi = "org.scaldi" %% "scaldi" % "latest.integration";
  lazy val scalaFx = "org.scalafx" %% "scalafx" % "8.0.102-R11"
  lazy val typesafeConfigs = "com.iheart" %% "ficus" % "latest.integration"
  lazy val enumeratum = "com.beachape" %% "enumeratum" % "latest.integration"
}
