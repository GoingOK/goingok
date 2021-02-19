package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{table, _}

object SidePanel {

  def groups(groups: List[String]): TypedTag[String] = {
    div(id:="groups", `class`:="side-panel show",
      div(`class`:="input-group",
        div(`class`:="col p-3",  p(`class`:="h5 side-panel-title", "All groups"),
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
        ),
        div(`class`:="input-group-append", button(`class`:="btn btn-dark", id:="side-panel-close-btn", i(`class`:="fas fa-chevron-left"))
        )
      )
    )
  }

  def btn(): TypedTag[String] = {
    div(`class`:="side-panel-btn-container", button(`class`:="btn btn-dark side-panel-btn", id:="side-panel-open-btn", i(`class`:="fas fa-chevron-right")))
  }
}
