package views.components.profile

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, _}

object Network {
  def display(groupExp: Boolean): TypedTag[String] ={
    div(`class` := "card",
      div(`class` := "card-body",
        h5(`class` := "card-title",
          span("Your reflections network"),
          button(`type` := "button", `class` := "btn btn-light btn-sm float-end",
            i(`class` := "fas fa-question-circle")
          )
        ),
        h6(`class` := "card-subtitle mb-2 d-flex",
          groupTags(groupExp),
          zoomButtons(groupExp)
        ),
        div(`class` := "chart-container network"),
          div(`class` := "d-flex mt-3",
            ul(id := "tags", `class` := "list-unstyled d-flex m-0")
          )
      )
    )
  }

  private def groupTags(groupExp: Boolean): TypedTag[String] = {
    div(`class` := "me-auto",
      if (groupExp) {
        div(id := "group-tags-div", `class` := "form-check my-auto me-auto",
          div(`class` := "group-tags-check-div on",
            input(id := "group-tags", `class` := "group-tags-check-input", `type` := "checkbox", value := "group-tags", checked)
          ),
          label(`class` := "form-check-label", `for` := "group-tags", "Group tags")
        )
      } else {}
    )
  }

  private def zoomButtons(groupExp: Boolean): TypedTag[String] = {
    div(
      if (groupExp) {
        div(`class` := "d-flex",
          span(`class` := "ms-auto my-auto me-2", "zoom:"),
          div(`class` := "input-group input-group-sm zoom-buttons",
            button(`class` := "btn btn-secondary btn-sm my-auto", id := "zoom-minus", "-"),
            input(`type` := "text", value := "100%", `class` := "form-control text-center", id := "zoom-number", disabled := "True"),
            button(`class` := "btn btn-secondary btn-sm my-auto", id := "zoom-plus", "+")
          )
        )
      } else {}
    )
  }
}
