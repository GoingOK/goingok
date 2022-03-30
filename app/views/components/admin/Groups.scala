package views.components.admin

import org.goingok.server.data.AdminData
import org.goingok.server.data.DbResults.Supervisors
import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.GenericComponents

object Groups extends GenericComponents {

  def display(adminData:AdminData): TypedTag[String] = {
    card("Groups",
      div(groupList(adminData.groupInfo),
        groupForm,
        groupAdminList(adminData.groupAdminInfo),
        groupAdminForm(adminData.supervisors, adminData.groupInfo)
      )
    )
  }

  private def groupList(groupInfo:Seq[(String,Int)]): TypedTag[String] ={
    div(style:="height: 233px; overflow-y: scroll; border: 1px solid black;",
      div(`class`:="group-summary-text small",b("Total number groups: "),groupInfo.size),
      table(`class`:="table table-striped table-sm",
        thead(tr(th(small(b("group code"))),th(small(b("admin"))),th(small(b("authors"))),th(small(b("reflections"))))),
        tbody(
          groupInfo.map{ case (group_code:String,not_used:Int) =>
            tr(td(small(group_code)),td(small("not implemented")),td(small(99)),td(small(9999)))
          }
        )
      )
    )
  }

  private def groupForm: TypedTag[String] = {
    form(`class` := "needs-validation", action := "/admin/addGroup", method := "POST", attr("novalidate") := "",
      div(`class` := "form-group",
        label(`for` := "group-code", `class` := "col-form-label")("Enter a new group code:"),
        input(`type` := "text", name := "group-code", `class` := "form-control", id := "group-code", attr("required") := ""),
        div(`class` := "invalid-feedback", "A group-code is required.")
      ),
      div(`class` := "form-group text-right",
        input(`type` := "submit", value := "add", `class` := "btn btn-success")
      ),
      script(validationScript)
    )
  }

  private def groupAdminList(groupAdminInfo:Seq[(String,String,String)]): TypedTag[String] = {
    val admins = uniqueAdmins(groupAdminInfo)
    div(style:="height: 233px; overflow-y: scroll; border: 1px solid black;",
      div(`class`:="groupadmin-summary-text small",b("Total number admins: "),admins.size),
      table(`class`:="table table-striped table-sm",
        thead(tr(th(small(b("admin email"))),th(small(b("pseudonym"))),th(small(b("group"))))),
        tbody(
          groupAdminInfo.map{ case (group:String,pseudonym:String,email:String) =>
            tr(td(small(email)),td(small(pseudonym)),td(small(group)))
          }
        )
      )
    )
  }

  private def groupAdminForm(supervisors: Seq[(String,String,Boolean)],groupInfo:Seq[(String,Int)]): TypedTag[String] = {
    form(`class` := "needs-validation", action := "/admin/addGroupAdmin", method := "POST", attr("novalidate") := "",
      div(`class` := "form-group",
        label(`for` := "admin-pseudonym", `class` := "col-form-label")("Admin:"),
        select(id := "admin-pseudonym", name := "admin",
          supervisors.map{ case (pseudonym:String,group_code:String,supervisor:Boolean) =>
            option(value:=s"$pseudonym")(s"$pseudonym ($group_code)")
          }.toList
        )
      ),
      div(`class` := "form-group",
        label(`for` := "admin-group", `class` := "col-form-label")("Group:"),
        select(id := "admin-group", name := "group",
          uniqueGroups(groupInfo).map{ case (group:String) =>
            option(value:=s"$group")(s"$group")
          }.toList
        )
      ),
      div(`class` := "form-group text-right",
        input(`type` := "submit", value := "add", `class` := "btn btn-success")
      ),
      script(validationScript)
    )
  }

//  private def groupAdminForm(groupAdminInfo:Seq[(String,String,String)],groupInfo:Seq[(String,Int)]): TypedTag[String] = {
//    form(`class` := "needs-validation", action := "/admin/addGroupAdmin", method := "POST", attr("novalidate") := "",
//      div(`class` := "form-group",
//        label(`for` := "admin-pseudonym", `class` := "col-form-label")("Admin:"),
//        select(id := "admin-pseudonym", name := "admin",
//          uniqueAdmins(groupAdminInfo).map{ case (pseudonym:String,email:String) =>
//            option(value:=s"$pseudonym")(s"$pseudonym ($email)")
//          }.toList
//        )
//      ),
//      div(`class` := "form-group",
//        label(`for` := "admin-group", `class` := "col-form-label")("Group:"),
//        select(id := "admin-group", name := "group",
//          uniqueGroups(groupInfo).map{ case (group:String) =>
//            option(value:=s"$group")(s"$group")
//          }.toList
//        )
//      ),
//      div(`class` := "form-group text-right",
//        input(`type` := "submit", value := "add", `class` := "btn btn-success")
//      ),
//      script(validationScript)
//    )
//  }
//
  private def uniqueAdmins(groupAdminInfo:Seq[(String,String,String)]):Seq[(String,String)] = groupAdminInfo.map(g => (g._2,g._3)).distinct
  private def uniqueGroups(groupInfo:Seq[(String,Int)]):Seq[String] = groupInfo.map(_._1).distinct
}
