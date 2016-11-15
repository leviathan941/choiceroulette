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

import Dependencies._

val srcPath: File = file(".")

lazy val commonSettings: Seq[Setting[_]] = Seq(
  scalaVersion := scala212,
  version := "0.1.0-SNAPSHOT",
  organization := "org.leviathan941",
  scalacOptions += "-feature",
  licenses += ("Apache 2.0", url("https://opensource.org/licenses/Apache-2.0")),

  javaHome := {
    Option(System.getenv("JAVA_HOME")) match {
      case Some(path) if file(path).exists => Option(file(path))
      case _ => throw new RuntimeException("No JDK found - try to set 'JAVA_HOME' environment variable.")
    }
  }
  )

lazy val root: Project = (project in srcPath).
  aggregate(guiApp).
  settings(commonSettings: _*).
  settings(
    name := "choiceroulette",
    description := "Choice roulette application"
  )

lazy val guiApp: Project = (project in srcPath / "guiapp").
  settings(commonSettings: _*).
  settings(
    name := "choiceroulette-cli",
    description := "GUI for choice roulette",
    libraryDependencies ++= Seq(
      scalaFx withJavadoc() withSources()
    ),

    mainClass in (Compile, run) := Some("choiceroulette.gui.GuiApplication"),
    mainClass in (Compile, packageBin) := Some("choiceroulette.gui.GuiApplication"),

    mainClass in assembly := Some("choiceroulette.gui.GuiApplication"),
    assemblyJarName in assembly := name.value + "-" + version.value + ".jar"
  )
