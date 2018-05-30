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

lazy val serverName = "goingok"
lazy val clientName = "clientJS"
lazy val sharedName = "sharedJS"

lazy val serverVersion = projectVersion
lazy val clientVersion = projectVersion
lazy val sharedVersion = projectVersion

//Dependencies

lazy val playJsonVersion = "2.6.9"
lazy val scalaTagsVersion = "0.6.7"
lazy val playSilhouetteVersion = "5.0.4"

lazy val scalatestVersion = "3.0.5"
lazy val scalatestPlayVersion = "3.1.2"

lazy val googleClientApiVersion = "1.23.0"
lazy val postgresDriverVersion = "42.2.2"
lazy val json4sVersion = "3.5.3"
lazy val slickVersion = "3.2.1"
lazy val slickpgVersion = "0.16.1"
lazy val playSlickVersion = "3.0.3"

lazy val scalaJsDomVersion = "0.9.5"
lazy val scalaJsD3Version = "0.3.4"
lazy val scalaJsBootstrapVersion = "2.3.1"

val generalDependencies = Seq(
  "com.typesafe.play" %% "play-json" % playJsonVersion,
  "com.lihaoyi" %% "scalatags" % scalaTagsVersion,
   //,
  //"com.atlassian.jwt" % "jwt-core" % "1.6.1",
  //"com.atlassian.jwt" % "jwt-api" % "1.6.1"
)

val silhouetteDependencies = Seq(
  "com.mohiva" %% "play-silhouette" % playSilhouetteVersion,
  "com.mohiva" %% "play-silhouette-password-bcrypt" % playSilhouetteVersion,
  "com.mohiva" %% "play-silhouette-persistence" % playSilhouetteVersion,
  "com.mohiva" %% "play-silhouette-crypto-jca" % playSilhouetteVersion,
  "net.codingwell" %% "scala-guice" % "4.2.1", //extention to guice DI
  "com.iheart" %% "ficus" % "1.4.3", //extention to typesafe config
  "com.mohiva" %% "play-silhouette-testkit" % playSilhouetteVersion % "test",
  "javax.xml.bind" % "jaxb-api" % "2.3.0", //to fix issue with missing xml in Java9
  "net.minidev" % "json-smart" % "2.3"
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

lazy val play = (project in file("."))
  .settings(
    commonSettings,
    name := projectName,
    version := projectVersion,
    scalaJSProjects := Seq(clientJS),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= silhouetteDependencies,
    libraryDependencies ++= Seq(ws, guice, ehcache, specs2 % Test),
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value,
    dockerExposedPorts := Seq(9000,80), // sbt docker:publishLocal
    dockerRepository := Some(s"$dockerRepoURI"),
    defaultLinuxInstallLocation in Docker := "/opt/docker",
    dockerExposedVolumes := Seq("/opt/docker/logs"),
    dockerBaseImage := "openjdk:9-jdk"
  ).enablePlugins(PlayScala,WebScalaJSBundlerPlugin,SbtWeb)
  .dependsOn(server,sharedJvm)

lazy val server = (project in file(serverName))
  .settings(
    resolvers += Resolver.bintrayRepo("nlytx", "nlytx-nlp"),
    //resolvers += Resolver.sonatypeRepo("releases"),
    resolvers += Resolver.jcenterRepo,
    libraryDependencies ++= generalDependencies ++ dbDependencies ++ googleDependencies ++ testDependencies,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := projectOrg,
    buildInfoOptions += BuildInfoOption.BuildTime,
  ).enablePlugins(BuildInfoPlugin)
  .dependsOn(sharedJvm)

//lazy val goingok_server = (project in file(serverName))
//  .settings(
//    commonSettings,
//    name := serverName,
//    version := serverVersion,
//    scalaJSProjects := Seq(goingok_client),
//    pipelineStages in Assets := Seq(scalaJSPipeline),
//    pipelineStages := Seq(digest, gzip),
//    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
//    libraryDependencies ++= Seq(ws, guice),
//    libraryDependencies ++= generalDependencies ++ dbDependencies ++ googleDependencies ++ testDependencies,
//    WebKeys.packagePrefix in Assets := "public/",
//    managedClasspath in Runtime += (packageBin in Assets).value,
//    PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value,
//    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
//    buildInfoPackage := projectOrg,
//    buildInfoOptions += BuildInfoOption.BuildTime
//  ).enablePlugins(PlayScala,BuildInfoPlugin)
//  .disablePlugins(PlayLayoutPlugin)
//  .dependsOn(sharedJvm)

lazy val clientJS = (project in file(clientName))
  .settings(
    commonSettings,
    name := clientName,
    version := clientVersion,
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % scalaJsDomVersion,
      "org.singlespaced" %%% "scalajs-d3" % scalaJsD3Version,
      "com.github.karasiq" %%% "scalajs-bootstrap-v4" % "2.3.1",
      "me.shadaj" %%% "slinky-core" % "0.4.2", // core React functionality, no React DOM
      "me.shadaj" %%% "slinky-web" % "0.4.2" // React DOM, HTML and SVG tags
    ),
    npmDependencies in Compile ++= Seq(
      "bootstrap" -> "4.1.1",
      //"font-awesome5" -> "1.0.5",
      "d3" -> "5.4.0",
      "react" -> "16.2.0",
      "react-dom" -> "16.2.0"
    )
  ).enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val sharedJS = (crossProject.crossType(CrossType.Pure) in file(sharedName))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq("com.lihaoyi" %%% "scalatags" % scalaTagsVersion)
  )

lazy val sharedJvm = sharedJS.jvm
lazy val sharedJs = sharedJS.js




// loads the server project at sbt startup
//onLoad in Global := (onLoad in Global).value andThen {s: State => s"project $serverName" :: s}


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