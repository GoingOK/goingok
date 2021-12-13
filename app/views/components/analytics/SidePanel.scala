package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{_}

object SidePanel {
  def display(): TypedTag[String] = {
    div(id:="sidebar", `class`:= "active sticky-top",
      div(`class`:="row mr-0",
        div(`class`:="col-md-10 pr-0",
          div(`class`:="sidebar-header",
            h3("Your groups")
          ),
          ul(id:="groups", `class`:="list-unstyled components")
        ),
        div(`class`:="col-md-2 p-0", id:="analytics-navbar",
          button(`type`:="button", id:="sibebar-btn", `class`:="btn btn-dark my-4",
            i(`class`:="fa fa-align-justify")
          )
        )
      )
    )
  }
}
