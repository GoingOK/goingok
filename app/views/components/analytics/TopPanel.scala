package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{_}

object TopPanel {
  def display(control: Boolean): TypedTag[String] = {
    div(`class`:= "navbar navbar-expand-lg navbar-light bg-light",
      div(`class`:="container-fluid",
        button(`class`:="btn btn-info", id:="sidebar-btn", i(`class`:="fa fa-align-justify mr-2"), "Toggle Groups Bar"),
        if (control){
          a(`class`:="btn btn-primary", href:="/analytics", "Switch to experiment")
        } else {
          a(`class`:="btn btn-primary", href:="/analytics?control=true", "Switch to control")
        }
      )
    )
  }
}
