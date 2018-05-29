package views.components

object Charts {
  import scalatags.Text.all._
  import scalatags.Text.tags2

  val demoHistogram = div(id :="d3element")

  val reflectionPoints = div( id := "container", `class` := "svg-container","This is where the chart goes.",
    div(id := "test-js-content","This should be replaced by text from ClientJS"),
    //div(id := "d3element")
  )


}

