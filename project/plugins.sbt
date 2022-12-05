ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always

//Play framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.18")

//ScalaJS
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.12.0")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.2.0")
addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % "1.0.2")

//Dependency management
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.3")
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

//Build and packaging
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")

//Documentation
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.4.1")
addSbtPlugin("com.github.sbt" % "sbt-unidoc" % "0.5.0")

//Code quality
//addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

