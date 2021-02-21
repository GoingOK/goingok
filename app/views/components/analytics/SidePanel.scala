package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{_}

object SidePanel {

  def display(groups: List[String]): TypedTag[String] = {
    div(id:="sidebar",
      div(`class`:="sidebar-header",
        h3("Your groups")
      ),
      div(id:="groups",
          table(`class`:="table table-striped table-borderless table-sm",
            thead(
              tr(th(attr("scope"):="col", "Group"), th(attr("scope"):="col", "Visualise"), th(attr("scope"):="col", "Download"))
            ),
            tbody(
              for(group <- groups) yield tr(
                td(group),
                td(`class`:="text-center", div(`class`:="form-check",
                  input(`class`:="form-check-input", `type`:="checkbox", `value`:=group, id:=group),
                  label(`class`:="form-check-label", `for`:=group))),
                td(`class`:="text-center", a(href:=s"/analytics/csv?group=$group", i(`class`:="fas fa-download"))
                )
              )
            )
          )
        )
      )
  }

  def btn(): TypedTag[String] = {
    button(`class`:="btn btn-dark", id:="sidebar-btn", i(`class`:="fa fa-align-justify"))
  }
}
