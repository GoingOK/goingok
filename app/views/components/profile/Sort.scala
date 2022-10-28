package views.components.profile

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, id, _}

object Sort {
  def display(): TypedTag[String] = {
    div(`class` := "card",
      div(`class` := "card-body",
        h6(`class` := "card-subtitle",
          span(`class` := "my-auto", "Sort reflections by:"),
          div(`class` := "btn-group btn-group-toggle", data("toggle") := "buttons",
            label(`class` := "btn btn-light active",
              input(`type` := "radio", name := "sort", value := "timestamp", checked, "Date",
                i(`class`:= "fa fa-chevron-down")
              )
            ),
            label(`class` := "btn btn-light",
              input(`type` := "radio", name := "sort", value := "point", checked, "Point",
                i(`class`:= "fa fa-chevron-down d-none")
              )
            )
          )
        )
      )
    )
  }
}
