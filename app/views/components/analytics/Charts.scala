package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, id, _}

object Charts {

  def display(): TypedTag[String] = {
    div(id :="analytics-charts", `class`:="row",
      div(`class`:="col-md-6", id:="groups-chart",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Selected groups reflections distribution - Box Plot"),
              button(`type`:="button", `class`:="btn btn-light btn-sm float-right",
                i(`class`:="fas fa-question-circle")
              )
            ),
            h6(`class`:="card-subtitle mb-2 instructions"),
            div(`class`:="chart-container")
          )
        )
      ),
      div(`class`:="col-md-6", id:="group-histogram-chart",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Selected groups reflections distribution - Histogram"),
              button(`type`:="button", `class`:="btn btn-light btn-sm float-right",
                i(`class`:="fas fa-question-circle")
              )
            ),
            h6(`class`:="card-subtitle mb-2 instructions"),
            div(`class`:="chart-container")
          )
        )
      ),
      div(`class`:="col-md-12 mt-3", id:="group-timeline",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Selected groups reflections against time - Timeline"),
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
      div(`class`:="col-md-6 mt-3", id:="group-histogram-users-chart",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Selected groups users distribution - Histogram"),
              button(`type`:="button", `class`:="btn btn-light btn-sm float-right",
                i(`class`:="fas fa-question-circle")
              )
            ),
            h6(`class`:="card-subtitle mb-2 instructions"),
            div(`class`:="chart-container")
          )
        )
      ),
      div(`class`:="col-md-6 mt-3", id:="user-statistics",
        div(`class`:="card",
          div(`class`:="card-body",
            h5(`class`:="card-title",
              span("Users compared to their group"),
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
