package views.components.profile

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, value, _}

object Reflections {
  def display(groupExp: Boolean): TypedTag[String] ={
    div(`class` := "card",
      div(`class` := "card-body",
        h5(`class` := "card-title",
          span("Your reflections"),
          button(`type` := "button", `class` := "btn btn-light btn-sm float-end",
            i(`class` := "fas fa-question-circle")
          )
        ),
          h6(`class` := "card-subtitle d-flex mb-2",
            div(`class` := "col-md-6 pl-0 text-muted"),
            sortReflections(groupExp)
          ),
        div(`class` := "reflections-tab")
      )
    )
  }


  private def sortReflections(groupExp: Boolean): TypedTag[String] = {
    div(`class` := "col-md-6 d-flex",
      if (groupExp) {
        div(id := "sort-reflections", `class` := "ms-auto",
          span(`class` := "my-auto", "Sort by:"),
          div(`class` := "btn-group sort-by", role := "group",
            input(`type` := "radio", `class` := "btn-check", name := "sort", id := "sort-timestamp", value := "timestamp", checked),
            label(`class` := "btn btn-outline-secondary", `for` := "sort-timestamp", "Date", i(`class` := "fa fa-chevron-down")),
            input(`type` := "radio", `class` := "btn-check", name := "sort", id := "sort-point", value := "point"),
            label(`class` := "btn btn-outline-secondary", `for` := "sort-point", "Point", i(`class` := "fa fa-chevron-down d-none"))
          )
        )
      } else {}
    )
  }
}
