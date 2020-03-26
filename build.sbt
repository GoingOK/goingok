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

//scalacOptions += "-Ypartial-unification" // 2.11.9+
//scalacOptions += "-target:jvm-1.8"

lazy val projectName = "goingok"
lazy val projectVersion = "4.2.3"
lazy val projectOrganisation = "org.goingok"

lazy val serverName = s"${projectName}_server"
lazy val clientName = s"${projectName}_client"
lazy val sharedName = s"${projectName}_shared"

//Versions
scalaVersion in ThisBuild := "2.13.1"

//resolvers in ThisBuild += "nlytx bintray" at "https://dl.bintray.com/nlytx/nlytx-nlp"
//resolvers in ThisBuild += Resolver.bintrayRepo("nlytx", "nlytx-nlp")

lazy val vScalaTags = "0.8.6"
lazy val vSlinky = "0.6.4"
lazy val vSjsD3 = "0.3.4"

lazy val vUpickle = "1.0.0"
lazy val vGoogleClientApi = "1.30.9"
lazy val vDoobie = "0.8.8"
lazy val vConfig = "1.4.0"
//lazy val vNlpCommons = "1.1.2"

lazy val vScalaJsDom = "1.0.0"
lazy val vWebpack = "4.10.2"
lazy val vWebpackDevServer = "3.1.4"
//lazy val vSjsBootstrap = "2.3.5"
//lazy val vBootstrapNative = "1.2.7"

lazy val vReact = "16.13.0"
//lazy val vBootstrap = "4.1.1"
lazy val vJquery = "3.4.1"
lazy val vPopper = "1.14.4"
lazy val vD3 = "5.9.7"

lazy val vScalaTest = "3.1.1"
lazy val vScalaLogging = "3.9.2"

//Settings
val sharedSettings = Seq(
  organization := projectOrganisation,
  version := projectVersion
)

//Dependencies

val playDeps = Seq(ws, guice, ehcache) //, specs2 % Test)

val generalDeps = Seq(
  "com.typesafe" % "config" % vConfig,
  "com.lihaoyi" %% "scalatags" % vScalaTags, //Using ScalaTags instead of Twirl
  "com.lihaoyi" %% "upickle" % vUpickle, //Using uJson for main JSON
  //"io.nlytx" %% "nlytx-nlp-commons" % vNlpCommons
)

val authDeps = Seq(
    "com.google.api-client" % "google-api-client" % vGoogleClientApi,
)

val dbDeps = Seq(
  "org.tpolecat" %% "doobie-core" % vDoobie,
  "org.tpolecat" %% "doobie-postgres"  % vDoobie, // Postgres driver 42.2.2 + type mappings
  "org.tpolecat" %% "doobie-scalatest" % vDoobie  // ScalaTest support for typechecking statements.
)

val testDeps = Seq(
  "org.scalactic" %% "scalactic" % vScalaTest,
  "org.scalatest" %% "scalatest" % vScalaTest % "test"
)



lazy val goingok = project.in(file("."))
  .dependsOn(server,client)
  .aggregate(server,client)
  .settings(
    sharedSettings,
    libraryDependencies ++= playDeps,
    libraryDependencies ++= generalDeps,
    libraryDependencies ++= authDeps,
    libraryDependencies ++= testDeps,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    //resolvers += Resolver.sonatypeRepo("releases"),

    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline), //Needed for WebScalaJsBundler

    dockerExposedPorts := Seq(9000,80), // sbt docker:publishLocal
    dockerRepository := Some(s"$dockerRepoURI"),
    defaultLinuxInstallLocation in Docker := "/opt/docker",
    dockerExposedVolumes := Seq("/opt/docker/logs"),
    dockerBaseImage := "openjdk:11-jdk",
    // Puts unified scaladocs into target/api
    siteSubdirName in ScalaUnidoc := "api",
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc)
  ).enablePlugins(PlayScala)
  .enablePlugins(WebScalaJSBundlerPlugin)
  .enablePlugins(SiteScaladocPlugin,ScalaUnidocPlugin)

lazy val docs = (project in file("project_docs"))
  .settings(
    sharedSettings,
    publishArtifact := false,
    // sbt-site needs to know where to find the paradox site
    paradoxTheme := Some(builtinParadoxTheme("generic")),
    paradoxProperties ++= Map(
      "github.base_url" -> s"$githubBaseUrl",
      "scaladoc.api.base_url" -> s"$scaladocApiBaseUrl"
    ),
  ).enablePlugins(ParadoxSitePlugin)

lazy val server = project.in(file(serverName))
  .settings(
    sharedSettings,
    libraryDependencies ++= generalDeps,
    libraryDependencies ++= dbDeps,
    libraryDependencies ++= authDeps,
    libraryDependencies ++= testDeps,
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % vScalaLogging,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := projectOrganisation,
    buildInfoOptions += BuildInfoOption.BuildTime,
  ).enablePlugins(BuildInfoPlugin)

lazy val client = project.in(file(clientName))
  .settings(
    sharedSettings,
    scalaJSUseMainModuleInitializer := true,
    webpackBundlingMode := BundlingMode.LibraryAndApplication(), //Needed for top level exports
    version in webpack := vWebpack, // Needed for version 4 webpack
    version in startWebpackDevServer := vWebpackDevServer, // Needed for version 4 webpack
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % vScalaJsDom,
      "com.lihaoyi" %%% "scalatags" % vScalaTags, //Using ScalaTags instead of Twirl
      "com.lihaoyi" %%% "upickle" % vUpickle, //Using uJson for main JSON
      "me.shadaj" %%% "slinky-core" % vSlinky, // core React functionality, no React DOM
      "me.shadaj" %%% "slinky-web" % vSlinky, // React DOM, HTML and SVG tags
      "org.scalactic" %%% "scalactic" % vScalaTest,
      "org.scalatest" %%% "scalatest" % vScalaTest % "test"
    ),
    npmDependencies in Compile ++= Seq( //Only managing libraries that are used by ScalaJS, others are in public/javascripts and loaded in Includes (views)
      "d3" -> vD3,
      "react" -> vReact,
      "react-dom" -> vReact
    )
  ).enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin) //, ScalaJSWeb)

//Documentation
//Task for building docs and copying to root level docs folder (for GitHub pages)
val updateDocsTask = TaskKey[Unit]("updateDocs","copies paradox docs to /docs directory")

updateDocsTask := {
  val siteResult = makeSite.value
  val apiSource = new File("target/site")
  val paradoxSource = new File("project_docs/target/site")
  val docDest = new File("docs")
  IO.copyDirectory(apiSource,docDest,overwrite=true,preserveLastModified=true)
  IO.copyDirectory(paradoxSource,docDest,overwrite=true,preserveLastModified=true)
}

