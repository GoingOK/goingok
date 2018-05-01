package org.goingok.server.views.components

object Charts {
  import scalatags.Text.all._
  import scalatags.Text.tags2
  import controllers.routes.Assets

  val demoHistogram = div(id :="d3element")

  val reflectionPoints = div( id := "container", `class` := "svg-container","This is where the chart goes.")


}

