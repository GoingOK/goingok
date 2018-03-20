// Copyright (C) 2018 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import LocalSbtSettings._

name := "goingok"

version := "4.0.3"

scalaVersion := "2.12.4"

organization := "org.goingok"

lazy val playJsonVersion = "2.6.8"
lazy val scalaTagsVersion = "0.6.7"

lazy val scalatestVersion = "3.0.5"
lazy val scalatestPlayVersion = "3.1.2"

lazy val googleClientApiVersion = "1.23.0"
lazy val postgresDriverVersion = "42.2.1"
lazy val json4sVersion = "3.5.3"
lazy val slickVersion = "3.2.1"
lazy val slickpgVersion = "0.15.7"
//lazy val slf4jVersion = "1.7.25"


enablePlugins(PlayScala)
disablePlugins(PlayLayoutPlugin)
PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value
libraryDependencies += ws     //Http client
libraryDependencies += guice  //Dependency injection

val generalDependencies = Seq(
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  "com.lihaoyi" %% "scalatags" % scalaTagsVersion
)

// Database Dependencies
val dbDependencies = Seq(
  "org.postgresql" % "postgresql" % postgresDriverVersion,
  "com.typesafe.play" %% "play-slick" % "3.0.3",
  "com.github.tminglei" %% "slick-pg" % slickpgVersion exclude("org.postgresql","postgresql"), //provided by postgresql
  "com.github.tminglei" %% "slick-pg_play-json" % slickpgVersion

)

//Google dependencies
val googleDependencies = Seq(
  "com.google.api-client" % "google-api-client" % googleClientApiVersion
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % scalatestPlayVersion % "test" //,
  //"com.typesafe.akka" % "akka-stream-testkit_2.12" % akkaStreamVersion
)

libraryDependencies ++= generalDependencies ++ dbDependencies ++ googleDependencies ++ testDependencies

scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/src/main/scala/root-doc.md")

//Set the environment variable for hosts allowed in testing
fork in Test := true
envVars in Test := Map("TAP_HOSTS" -> "localhost")

enablePlugins(BuildInfoPlugin)
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
  buildInfoPackage := "org.goingok"
  buildInfoOptions += BuildInfoOption.BuildTime

//Documentation - run ;paradox;copyDocs
enablePlugins(ParadoxPlugin) //Generate documentation with Paradox
paradoxTheme := Some(builtinParadoxTheme("generic"))
paradoxProperties in Compile ++= Map(
  "github.base_url" -> s"$githubBaseUrl",
  "scaladoc.api.base_url" -> s"$scaladocApiBaseUrl"
)
//Task for copying to root level docs folder (for GitHub pages)
val copyDocsTask = TaskKey[Unit]("copyDocs","copies paradox docs to /docs directory")
copyDocsTask := {
  val docSource = new File("target/paradox/site/main")
  val apiSource = new File("target/scala-2.12/api")
  val docDest = new File("docs")
  val apiDest = new File("docs/api")
  //if(docDest.exists) IO.delete(docDest)
  IO.copyDirectory(docSource,docDest,overwrite=true,preserveLastModified=true)
  IO.copyDirectory(apiSource,apiDest,overwrite=true,preserveLastModified=true)
}

enablePlugins(JavaAppPackaging) // sbt universal:packageZipTarball
dockerExposedPorts := Seq(9000) // sbt docker:publishLocal
dockerRepository := Some(s"$dockerRepoURI")
defaultLinuxInstallLocation in Docker := "/opt/docker"
dockerExposedVolumes := Seq("/opt/docker/logs")
dockerBaseImage := "openjdk:9-jdk"
javaOptions in Universal ++= Seq(
  // -J params will be added as jvm parameters
  "-J-Xmx4g",
  "-J-Xms2g"
)
