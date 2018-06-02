package views.components.profile

import scalatags.Text.all._

object ReflectionEntry {

  private def slider(point:Double) = div(`class`:="form-group",
    div(`class`:="d-flex justify-content-between",
      div( `class` := "p-2", "Distressed"),
      div( `class` := "p-2", "GoingOK"),
      div( `class` := "p-2", "Soaring")
    ),
    input(`type`:="range",`class`:="form-control-range",id:="formControlRange",value:=point)
  )

  def display(sliderPoint:Double=0.5) = form( `class` := "form-horizontal", id := "form", name := "form", method := "post",
    div( `class` := "row",
      div( `class` :="col",
        this.slider(sliderPoint)
      )
    ),
    hr(),
    div( `class` := "row", div("Describe how you are going...")),
    div( `class` := "row", textarea(`class`:="form-control", id := "describe", name := "describe", rows := "10", attr("ngModel") := "reflectText")),
    div( `class` := "d-flex flex-row-reverse", button( `type` := "submit", `class` := "p-2 btn btn-success", onclick := "alert('Form save clicked')","Save"))
  )
}
