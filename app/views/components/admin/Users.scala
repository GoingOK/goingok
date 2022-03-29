package views.components.admin

import org.goingok.server.data.AdminData
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.GenericComponents

object Users extends GenericComponents {

  def display(adminData:AdminData): TypedTag[String] = {
    card("Pseudonyms",
      div(userList(adminData.userInfo),
        pseudonymsForm,
        userForm(adminData.groupAdminInfo)
      )
    )
  }

  private def userList(userInfo:Seq[(String,Int)]): Seq[TypedTag[String]] = {
    userInfo.map { case (name:String,value:Int) =>
      div(b(name,":")," ",value)
    }
  }

  private def pseudonymsForm: TypedTag[String] = {
    form(`class` := "needs-validation", action := "/admin/addPseudonyms", method := "POST", attr("novalidate") := "",
      div(`class` := "form-group text-right",
        input(`type` := "submit", value := "add pseudonyms", `class` := "btn btn-success")
      ),
      script(validationScript)
    )
  }

  private def userForm(groupAdminInfo:Seq[(String,String,String)]): TypedTag[String] = {
    form(`class` := "needs-validation", action := "/admin/updateUsers", method := "POST", attr("novalidate") := "",
      div(`class` := "form-group",
        label(`for` := "user", `class` := "col-form-label")("User:"),
        select(id := "user", name := "user",
          uniqueAdmins(groupAdminInfo).map{ case (pseudonym:String,email:String) =>
            option(value:=s"$pseudonym")(s"$pseudonym ($email)")
          }.toList
        )
      ),
      div(`class` := "form-group form-row",
        div(`class`:="form-group col-md-6",
          label(`for`:="supervisor")("Is Supervisor"),
          input(`type`:="checkbox", id:="supervisor", name:="supervisor")
        ),
        div(`class`:="form-group col-md-6",
          label(`for`:="tester")("Is Tester"),
          input(`type`:="checkbox", id:="tester", name:="tester")
        )
      ),
      div(`class` := "form-group text-right",
        input(`type` := "submit", value := "update", `class` := "btn btn-success")
      ),
      script(validationScript)
    )
  }

  private def uniqueAdmins(groupAdminInfo:Seq[(String,String,String)]):Seq[(String,String)] = groupAdminInfo.map(g => (g._2,g._3)).distinct
}
