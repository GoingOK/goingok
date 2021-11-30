package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{_}

object TopPanel {
  def display(control: Boolean): TypedTag[String] = {
    div(id:= "analytics-navbar", `class`:= "navbar navbar-expand-lg navbar-light bg-light sticky-top",
      button(`class`:="btn btn-info", id:="sidebar-btn", i(`class`:="fa fa-align-justify mr-2"), "Toggle Groups Bar"),
      ul(`class`:="nav nav-pills mx-auto"),
      if (control){
        a(`class`:="btn btn-warning", href:="/analytics", "Switch to experiment")
      } else {
        a(`class`:="btn btn-warning", href:="/analytics?control=true", "Switch to control")
      }
    )
  }
}
