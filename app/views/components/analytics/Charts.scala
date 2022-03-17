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
        div(`class`:="col-md-12 mt-3", id:="sort",
          div(`class`:="card",
            div(`class`:="card-body",
              h6(`class`:="card-subtitle",
                span(`class`:= "mr-2", "Sort groups by:"),
                div(`class`:="btn-group btn-group-toggle", data("toggle"):="buttons",
                  label(`class`:="btn btn-light active",
                    input(`type`:="radio", name:="sort", value:="date", checked, "Create date")
                  ),
                  label(`class`:="btn btn-light",
                    input(`type`:="radio", name:="sort", value:="name", checked, "Name")
                  ),
                  label(`class`:="btn btn-light",
                    input(`type`:="radio", name:="sort", value:="mean", checked, "Mean")
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
            h6(`class`:="card-subtitle mb-2 instructions"),
            div(`class`:="row mt-3",
              div(id:="timeline-plot", `class`:="btn-group btn-group-toggle mx-auto", data("toggle"):="buttons",
                label(`class`:="btn btn-light active",
                  input(`type`:="radio", name:="plot", value:="scatter", "Scatter Plot")
                ),
                label(`class`:="btn btn-light",
                  input(`type`:="radio", name:="plot", value:="density", "Density Plot")
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
            h6(`class`:="card-subtitle mb-2 instructions")
          )
        )
      )
    )
  }
}
