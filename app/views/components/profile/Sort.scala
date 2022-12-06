package views.components.profile

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, id, _}

object Sort {
  def display(): TypedTag[String] = {
    div(`class` := "card",
      div(`class` := "card-body",
        h6(`class` := "card-subtitle",
          span(`class` := "my-auto", "Sort reflections by:"),
          div(`class` := "btn-group sort-by", role := "group",
            input(`type` := "radio", `class`:="btn-check", name := "sort", id:="sort-timestamp", value := "timestamp", checked),
            label(`class` := "btn btn-outline-secondary", `for`:="sort-timestamp", "Date", i(`class` := "fa fa-chevron-down")),
            input(`type` := "radio", `class`:="btn-check", name := "sort", id:="sort-point", value := "point"),
            label(`class` := "btn btn-outline-secondary", `for`:="sort-point", "Point", i(`class` := "fa fa-chevron-down d-none"))
          )
        )
      )
    )
  }
}
