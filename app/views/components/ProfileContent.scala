package views.components

import scalatags.Text.TypedTag
import scalatags.Text.all._

object ProfileContent {

  lazy val mainContainer = div(id := "profile-content",`class` := "container-fluid",
    div( id := "reflectchart-content", `class` := "row",
      div( `class` := "col-sm-12",
        panel("reflection-points","fas fa-chart-line","GoingOK over time",Charts.reflectionPoints)
      )
    ),
    div( id := "main-content", `class` := "row",
      div( id := "main-left-column", `class` := "col-sm-8",
        panel("reflection-entry","fas fa-edit","Enter a reflection",this.entryForm),
        panel("reflection-list", "fas fa-list-alt", "Past reflections", this.reflections)
      ),
      div( id := "main-right-column", `class` := "col-sm-4",
        panel("message-list", "fas fa-envelope", "Messages", this.messages)
      )
    )
  )

  lazy val entryForm = form( `class` := "form-horizontal", id := "form", name := "form", method := "post",
    div( `class` := "row",
      div( `class` :="col-xs-12",
        input( id := "point", name := "point", `type` := "hidden", value := "50" ),
        div( `class` := "top-spaced",
          this.slider
        )
      )
    ),
    div( `class` := "row",
      div( `class` := "col", "Distressed"),
      div( `class` := "col", "GoingOK"),
      div( `class` := "col", "Soaring")
    ),
    hr(),
    div( `class` := "row", div("Describe how you are going...")),
    div( `class` := "row", textarea( id := "describe", name := "describe", rows := "10", attr("ngModel") := "reflectText")),
    div( `class` := "row", button( `type` := "submit", `class` := "btn btn-success float-right", attr("click") := "saveEntry()","Save"))
  )

  lazy val slider = div(
    div( `class` := "top-spaced",
      div( id := "simple-slider", `class` := "dragdealer rounded-cornered",
        div( `class` :="red-bar handle",
          div("Slider here")
        )
      )
    )
  )

  def panel(idName:String,icon:String,title:String,content:TypedTag[String]):TypedTag[String] = div(id := idName, `class` := "card profile-panel",
    div(`class` := "card-header",
      span(`class` := icon), b(s" $title"),
      span(`class` := "fas fa-question-circle float-right")
    ),
    div(`class` := "card-body",
      content
    )
  )

  def reflections = div("Reflections go here")

  def messages = div("Messages go here")


}

