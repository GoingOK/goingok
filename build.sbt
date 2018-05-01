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

//Project details

lazy val projectName = "goingok"
lazy val projectOrg = "org.goingok"
lazy val projectVersion = "4.1.0"
lazy val projectScalaVersion = "2.12.6"

lazy val serverName = "goingok_server"
lazy val clientName = "goingok_client"
lazy val sharedName = "goingok_shared"

lazy val serverVersion = projectVersion
lazy val clientVersion = projectVersion
lazy val sharedVersion = projectVersion

//Dependencies

lazy val playJsonVersion = "2.6.9"
lazy val scalaTagsVersion = "0.6.7"
lazy val scalaJsDomVersion = "0.9.5"

lazy val scalatestVersion = "3.0.5"
lazy val scalatestPlayVersion = "3.1.2"

lazy val googleClientApiVersion = "1.23.0"
lazy val postgresDriverVersion = "42.2.2"
lazy val json4sVersion = "3.5.3"
lazy val slickVersion = "3.2.1"
lazy val slickpgVersion = "0.16.1"
lazy val playSlickVersion = "3.0.3"

val generalDependencies = Seq(
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  //"com.lihaoyi" %% "scalatags" % scalaTagsVersion
)

val dbDependencies = Seq(
  "org.postgresql" % "postgresql" % postgresDriverVersion,
  "com.typesafe.play" %% "play-slick" % playSlickVersion,
  "com.github.tminglei" %% "slick-pg" % slickpgVersion exclude("org.postgresql","postgresql"), //provided by postgresql
  "com.github.tminglei" %% "slick-pg_play-json" % slickpgVersion

)

val googleDependencies = Seq(
  "com.google.api-client" % "google-api-client" % googleClientApiVersion
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % scalatestPlayVersion % "test" //,
  //"com.typesafe.akka" % "akka-stream-testkit_2.12" % akkaStreamVersion
)


//Modules

lazy val commonSettings = Seq(
  scalaVersion := projectScalaVersion,
  organization := projectOrg
)

lazy val goingok_server = (project in file(serverName))
  .settings(
    commonSettings,
    name := serverName,
    version := serverVersion,
    scalaJSProjects := Seq(goingok_client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(ws, guice),
    libraryDependencies ++= generalDependencies ++ dbDependencies ++ googleDependencies ++ testDependencies,
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,
    PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := projectOrg,
    buildInfoOptions += BuildInfoOption.BuildTime
  ).enablePlugins(PlayScala,BuildInfoPlugin)
  .disablePlugins(PlayLayoutPlugin)
  .dependsOn(sharedJvm)

lazy val goingok_client = (project in file(clientName))
  .settings(
    commonSettings,
    name := clientName,
    version := clientVersion,
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion
    )
  ).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val goingok_shared = (crossProject.crossType(CrossType.Pure) in file(sharedName))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq("com.lihaoyi" %%% "scalatags" % scalaTagsVersion)
  )
lazy val sharedJvm = goingok_shared.jvm
lazy val sharedJs = goingok_shared.js

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => s"project $serverName" :: s}


/*

scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/src/main/scala/root-doc.md")

//Set the environment variable for hosts allowed in testing
fork in Test := true
envVars in Test := Map("GOINGOK_HOSTS" -> "localhost")

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
*/