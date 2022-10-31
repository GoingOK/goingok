package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{_}

object SidePanel {
  def display(exp: Boolean): TypedTag[String] = {
    div(id:="sidebar", `class`:= "active sticky-top",
      div(`class`:="row",
        div(`class`:="col-md-12 d-flex my-3",
          h3(`class`:= "mx-auto", "Your groups"),
          button(`type`:="button", id:="sidebar-btn", `class`:="btn btn-dark mr-2",
            i(`class`:="fa fa-align-justify")
          )
        ),
        div(`class`:= "col-md-12", id := "all-groups",
          if(exp){
            ul(id := "all", `class` := "list-unstyled components mb-0",
              li(
                div(`class` := "input-group mb-0",
                  div(`class` := "input-group-prepend",
                    div(`class` := "input-group-text group-row",
                      input(`type` := "checkbox", value := "all", checked)
                    )
                  ),
                  input(`type` := "text", value := "select all", `class` := "form-control group-row", disabled)
                )
              )
            )
          } else {},
          ul(id:="groups", `class`:="list-unstyled components")
        ),
        div(id:="switch-dashboard", `class`:="col-md-12 d-flex",
          if (exp) {
            a(`class`:="btn btn-warning btn-block m-3", href:="/analytics", "Switch dashboard")
          } else {
            a(`class`:="btn btn-warning btn-block m-3", href:="/analytics?exp=true", "Switch dashboard")
          }
        )
      ),
      div(`class`:="row",
        div(id:="old-analytics", `class`:="col-md-12 d-flex",a(`class`:="btn btn-default btn-block m-3", href:="/analytics_old", "Old Page"))
      )
    )
  }
}