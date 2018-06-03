package views.components.register

import scalatags.Text.all._

object GroupPopup {

  def display() = div(
    p("In order to use GoingOK, you need to register with an existing GoingOK group such as an organisational unit, a subject cohort, or a research group."),
    p("A ",b("GoingOK group code")," should have been given to you by someone responsible for your group."),
    p("Please enter this code below and click ",b("register.")),
    div( `class`:="card-footer text-muted",
      form( `class`:="text-center",
        div(`class`:="form-group",
          label(`for`:="group-code", `class`:="col-form-label")("GoingOK group code:"),
          input(`type`:="text", `class`:="form-control", id:="group-code")
        ),
        div(`class`:="form-group",
          button(`type`:="button", `class`:="btn btn-success")("register")
        )
      )
    )
  )

}
