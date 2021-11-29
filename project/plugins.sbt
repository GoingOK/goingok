resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

//Play framework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.8")

//ScalaJS
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.7.1")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.1.0")
addSbtPlugin("ch.epfl.scala" % "sbt-web-scalajs-bundler" % "0.20.0")

//Dependency management
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.1")
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

//Build and packaging
//addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.8.0-RC14") // Not using play framework version
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")

//Documentation
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.4.1")
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.3")

//Code quality
//addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

