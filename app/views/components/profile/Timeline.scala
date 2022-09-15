package views.components.profile

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, id, _}

object Timeline {
  def display(): TypedTag[String] = {
    div(`class` := "card",
      div(`class` := "card-body",
        h5(`class` := "card-title",
          span("Your reflections timeline"),
          button(`type` := "button", `class` := "btn btn-light btn-sm float-right",
            i(`class` := "fas fa-question-circle")
          )
        ),
        h6(`class` := "card-subtitle mb-2 instructions"),
        div(`class` := "chart-container")
      )
    )
  }
}
