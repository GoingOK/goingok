// Copyright (C) 2018 - 2022 the original author or authors.
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
import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown

lazy val projectName = "goingok"
lazy val projectVersion = "4.4.2"
lazy val projectOrganisation = "org.goingok"

lazy val serverName = s"${projectName}_server"
lazy val clientName = s"${projectName}_client"
lazy val sharedName = s"${projectName}_shared"

//Versions
ThisBuild / scalaVersion := "2.13.10"

lazy val vUpickle = "2.0.0"
lazy val vDoobie = "0.13.4"
lazy val vConfig = "1.4.2"
lazy val vScalaTest = "3.2.15"
lazy val vScalaLogging = "3.9.5"

lazy val vScalaTags = "0.11.1"
lazy val vScalaJsDom = "2.3.0"

lazy val vJquery = "3.6.1"
lazy val vD3 = "6.7.0"
lazy val vBootstrap = "5.2.3"
lazy val vFontawesome = "6.2.0"

//Settings
val sharedSettings = Seq(
  organization := projectOrganisation,
  version := projectVersion
)

//Dependencies
val playDeps = Seq(ws, guice, ehcache) //, guice specs2 % Test)

val generalDeps = Seq(
  "com.typesafe" % "config" % vConfig,
  "com.lihaoyi" %% "scalatags" % vScalaTags, //Using ScalaTags instead of Twirl
  "com.lihaoyi" %% "upickle" % vUpickle, //Using uJson for main JSON
)

val authDeps = Seq(
  "com.pauldijou" %% "jwt-core" % "5.0.0",
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

val logDeps = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % vScalaLogging
)

val jsDeps = Seq(
  "org.webjars" % "jquery" % vJquery / "jquery.js",
  "org.webjars" % "d3js" % vD3 / "d3.js",
  "org.webjars" % "bootstrap" % vBootstrap / "bootstrap.js",
  "org.webjars" % "font-awesome" % vFontawesome / "fontawesome.js",
)


// Main project
lazy val goingok = project.in(file("."))
  .dependsOn(server,client)
  .aggregate(server,client)
  .settings(
    sharedSettings,
    libraryDependencies ++= playDeps,
    libraryDependencies ++= generalDeps,
    libraryDependencies ++= authDeps,
    libraryDependencies ++= testDeps,
    resolvers ++= Resolver.sonatypeOssRepos("releases"),

    scalaJSProjects := Seq(client),
    Assets / pipelineStages := Seq(scalaJSPipeline),

    dockerExposedPorts := Seq(9000,80), // sbt docker:publishLocal
    dockerRepository := Some(s"$dockerRepoURI"),
    Docker / defaultLinuxInstallLocation := "/opt/docker",
    dockerExposedVolumes := Seq("/opt/docker/logs"),
    dockerBaseImage := "eclipse-temurin:11", //"adoptopenjdk:latest", //"openjdk:18-oracle", //"openjdk:stable",
    // Puts unified scaladocs into target/api
    ScalaUnidoc / siteSubdirName  := "api",
    addMappingsToSiteDir( ScalaUnidoc / packageDoc / mappings,  ScalaUnidoc / siteSubdirName)
  ).enablePlugins(PlayScala)
  .enablePlugins(SbtWeb)
  .enablePlugins(SiteScaladocPlugin,ScalaUnidocPlugin)

// Documentation subproject
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

// Server subproject
lazy val server = project.in(file(serverName))
  .settings(
    sharedSettings,
    libraryDependencies ++= generalDeps,
    libraryDependencies ++= dbDeps,
    libraryDependencies ++= authDeps,
    libraryDependencies ++= testDeps,
    libraryDependencies ++= logDeps,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := projectOrganisation,
    buildInfoOptions += BuildInfoOption.BuildTime,
  ).enablePlugins(BuildInfoPlugin)

// Client subproject
lazy val client = project.in(file(clientName))
  .settings(
    sharedSettings,
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.NoModule) },
    jsDependencies ++= jsDeps,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % vScalaJsDom,
      "com.lihaoyi" %%% "scalatags" % vScalaTags, //Using ScalaTags instead of Twirl
      "com.lihaoyi" %%% "upickle" % vUpickle, //Using uJson for main JSON
      "org.scalactic" %%% "scalactic" % vScalaTest,
      "org.scalatest" %%% "scalatest" % vScalaTest % "test"
    )
  ).enablePlugins(ScalaJSPlugin, ScalaJSWeb,JSDependenciesPlugin)

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

