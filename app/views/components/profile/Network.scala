package views.components.profile

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, _}

object Network {
  def display(): TypedTag[String] ={
    div(`class` := "card",
      div(`class` := "card-body",
        h5(`class` := "card-title",
          span("Reflections network"),
          button(`type` := "button", `class` := "btn btn-light btn-sm float-end",
            i(`class` := "fas fa-question-circle")
          )
        ),
        h6(`class` := "card-subtitle mb-2 d-flex",
//          span(`class`:= "ms-auto my-auto", "zoom:"),
//          div(`class`:= "input-group input-group-sm zoom-buttons",
//            button(`class` := "btn btn-secondary btn-sm", id:="zoom-minus", "-"),
//            input(`type` := "text", value := "100%", `class` := "form-control text-center", id := "zoom-number", disabled:="True"),
//            button(`class` := "btn btn-secondary btn-sm", id:="zoom-plus", "+")
//          )
        ),
        div(`class` := "chart-container network")
      )
    )
  }
}
