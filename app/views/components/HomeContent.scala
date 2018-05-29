package views.components

import scalatags.Text.all._

object HomeContent {

  val sjs = div(p(id :="sjs","..."))

  lazy val mainContainer = div(id := "homepage-content",`class` := "container",
    div(`class` := "row",
      div( `class` := "col-sm-12",
        h4("Personal reflections for people on a journey"),
        p("GoingOK collects personal autobiographic reflections from people who are undertaking change or transition. The web application allows users to indicate how they feel they are going, and write about why they feel that way. These short reflections are collected regularly over a period of time.")
      )
    )
  )
}
