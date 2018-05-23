package views.components

object ComponentTests {
  import scalatags.Text.all._
  import scalatags.Text.tags2

  val sjs = div(p(id :="sjs","..."))

  val d3 = div(id :="d3element")

  val mainContainer = div(`class` := "container-fluid",
    sjs,
    d3
  )

}
