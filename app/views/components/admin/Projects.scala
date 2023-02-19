package views.components.admin

import org.goingok.server.data.AdminData
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.GenericComponents
import views.components.admin.Groups.validationScript
import views.components.admin.Users.validationScript

object Projects extends GenericComponents {

  def display(adminData:AdminData): TypedTag[String] = {

    card("Projects",
      div(b("Interactive Visualisations"),div(""),testerList(adminData.testers),div(""),dashboardTestersForm,div(b("Test Event Post")), uiLoggingTest)
    )
  }

  private def uiLoggingTest: TypedTag[String] = {
    form(`class` := "needs-validation", action := "/analytics/logUIevent", method := "POST", attr("novalidate") := "",
      div(`class` := "form-group",
        input(`type` := "hidden", name := "event", id := "event", value := "This is a test event"),
      ),
      div(`class` := "form-group text-right",
        input(`type` := "submit", value := "post event", `class` := "btn btn-success")
      )
    )
  }

  private def dashboardTestersForm: TypedTag[String] = {
    form(`class` := "needs-validation", action := "/admin/addDashboardTester", method := "POST", attr("novalidate") := "",
      div(`class` := "form-group",
        label(`for` := "tester", `class` := "col-form-label")("Enter a pseudonym:"),
        input(`type` := "text", name := "tester", `class` := "form-control", id := "tester", attr("required") := ""),
        div(`class` := "invalid-feedback", "A pseudonym is required.")
      ),
      div(`class` := "form-group text-right",
        input(`type` := "submit", value := "add", `class` := "btn btn-success")
      ),
      script(validationScript)
    )
  }

  private def testerList(testers:Seq[String]): TypedTag[String] ={
    div(style:="height: 233px; overflow-y: scroll; border: 1px solid black;",
      div(`class`:="group-summary-text small",b("Total number of testers: "),testers.size),
      table(`class`:="table table-striped table-sm",
        //thead(tr(th(small(b("Testers"))))),
        tbody(
          testers.map{ case (tester:String) =>
            tr(td(small(tester)))
          }
        )
      )
    )
  }

}
