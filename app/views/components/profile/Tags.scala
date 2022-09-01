package views.components.profile

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, id, _}

object Tags {
  def display(): TypedTag[String] = {
    div(`class` := "card",
      div(`class` := "card-body",
        h6(`class` := "card-subtitle d-flex",
          span(`class` := "my-auto", "Tags"),
          ul(id := "tags", `class` := "list-unstyled d-flex m-0")
        )
      )
    )
  }
}
