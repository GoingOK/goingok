package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, id, _}

object Charts {

  def display(exp: Boolean): TypedTag[String] = {
    div(id :="analytics-charts", `class`:="row",
      div(`class`:="col-md-4", id:="users-total",
        div(`class`:="card",
          div(`class`:="card-body",
            h1(`class`:="card-title d-flex",
              span(`class`:="mx-auto")
            ),
            h5(`class`:="card-subtitle mb-2 text-muted d-flex",
              span(`class`:="mx-auto", "Total users")
            )
          )
        )
      ),
      div(`class`:="col-md-4", id:="ref-total",
        div(`class`:="card",
          div(`class`:="card-body",
            h1(`class`:="card-title d-flex",
              span(`class`:="mx-auto")
            ),
            h5(`class`:="card-subtitle mb-2 text-muted d-flex",
              span(`class`:="mx-auto", "Total reflections")
            )
          )
        )
      ),
      div(`class`:="col-md-4", id:="ru-rate",
        div(`class`:="card",
          div(`class`:="card-body",
            h1(`class`:="card-title d-flex",
              span(`class`:="mx-auto")
            ),
            h5(`class`:="card-subtitle mb-2 text-muted d-flex",
              span(`class`:="mx-auto", "Reflections per user")
            )
          )
        )
      ),
      if(exp){
        div(`class`:="col-md-12 mt-3", id:="sort-groups",
          div(`class`:="card",
            div(`class`:="card-body",
              h6(`class`:="card-subtitle",
                span(`class`:= "mr-2", "Sort groups by:"),
                div(`class`:="btn-group btn-group-toggle", data("toggle"):="buttons",
                  label(`class`:="btn btn-light active",
                    input(`type`:="radio", name:="sort", value:="date", "Create date",
                      i(`class`:= "fa fa-chevron-down")
                    )
                  ),
                  label(`class`:="btn btn-light",
                    input(`type`:="radio", name:="sort", value:="name", "Name",
                      i(`class`:= "fa fa-chevron-down d-none")
                    )
                  ),
                  label(`class`:="btn btn-light",
                    input(`type`:="radio", name:="sort", value:="mean", "Mean",
                      i(`class`:= "fa fa-chevron-down d-none")
                    )
                  )
                )
              )
            )
          )
        )
      } else {},
      div(`class`:="col-md-6 mt-3", id:="users",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Total users by group"),
              button(`type`:="button", `class`:="btn btn-light btn-sm float-right",
                i(`class`:="fas fa-question-circle")
              )
            ),
            h6(`class`:="card-subtitle mb-2 instructions"),
            div(`class`:="chart-container")
          )
        )
      ),
      div(`class`:="col-md-6 mt-3", id:="histogram",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Users distribution"),
              button(`type`:="button", `class`:="btn btn-light btn-sm float-right",
                i(`class`:="fas fa-question-circle")
              )
            ),
            h6(`class`:="card-subtitle mb-2 instructions"),
            div(`class`:="chart-container")
          )
        )
      ),
      div(`class`:="col-md-12 mt-3", id:="timeline",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Users reflection points timeline"),
              button(`type`:="button", `class`:="btn btn-light btn-sm float-right",
                i(`class`:="fas fa-question-circle")
              )
            ),
            h6(`class`:="card-subtitle mb-2",
              p(`class` := "instructions mb-1"),
              p(`class` := "text-muted mb-0"),
              div(`class` := "input-group input-group-sm zoom-buttons",
                span(`class` := "mr-2 my-auto", "zoom:"),
                div(`class` := "input-group-prepend",
                  div(`class` := "input-group",
                    input(`class` := "btn btn-secondary btn-sm", `type` := "button", value := "-", id := "zoom-minus")
                  )
                ),
                input(`type` := "text", value := "100%", `class` := "form-control text-center", id := "zoom-number", disabled := "True"),
                div(`class` := "input-group-append",
                  div(`class` := "input-group",
                    input(`class` := "btn btn-secondary btn-sm", `type` := "button", value := "+", id := "zoom-plus")
                  )
                )
              )
            ),
            div(`class`:="chart-container")
          )
        )
      ),
      div(`class`:="col-md-12 mt-3", id:="reflections",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Users reflections"),
              button(`type`:="button", `class`:="btn btn-light btn-sm float-right",
                i(`class`:="fas fa-question-circle")
              )
            ),
            h6(`class`:="card-subtitle d-flex mb-2",
              div(`class` := "col-md-6 pl-0 text-muted"),
              div(`class` := "col-md-6 d-flex",
                div(id := "sort-users", `class` := "ml-auto",
                  span("Sort users by:"),
                  div(`class` := "btn-group btn-group-toggle", data("toggle") := "buttons",
                    label(`class` := "btn btn-light active",
                      input(`type` := "radio", name := "sort", value := "name", "Name",
                        i(`class` := "fa fa-chevron-down")
                      )
                    ),
                    label(`class` := "btn btn-light",
                      input(`type` := "radio", name := "sort", value := "point", "Reflection state point",
                        i(`class` := "fa fa-chevron-down d-none")
                      )
                    )
                  )
                )
              )
            ),
            ul(`class` := "nav nav-tabs"),
            div(`class` := "tab-content users-tab-pane")
          )
        )
      )
    )
  }
}
