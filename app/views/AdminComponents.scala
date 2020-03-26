package views

import org.goingok.server.data.AdminData
import scalatags.Text.TypedTag
import scalatags.Text.all._

class AdminComponents(adminData:AdminData) extends GenericComponents {

  def projectAdmin:CardComponent = {
    val content = div("Administer projects here...")
    card("Projects",content)
  }

  def groupsAdmin:CardComponent = {
    val content = div(groupList(adminData.groupInfo),groupForm)
    card("Groups",content)
  }

  def usersAdmin:CardComponent = {
    val content = div(userList(adminData.userInfo),userForm)
    card("Pseudonyms",content)
  }

  /** Group list HTML display */
  private def groupList(groupInfo:Seq[(String,Int)]):TypedTag[String] = {
    div(style:="height: 233px; overflow-y: scroll; border: 1px solid black;",
      table(`class`:="table table-striped table-sm",
        thead(tr(th(small(b("group code"))),th(small("admin name")),th(small("authors")),th(small("reflections")))),
        tbody(
          groupInfo.map{ case (group_code:String,not_used:Int) =>
            tr(td(small(group_code)),td(small("not implemented")),td(small(99)),td(small(9999)))
          }
        )
      )
    )
  }

  /** Group form HTML display */
  private def groupForm:TypedTag[String] = {
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

  /** Group form HTML display */
  private def userList(userInfo:Seq[(String,Int)]):Seq[TypedTag[String]] = {
    userInfo.map { case (name:String,value:Int) =>
      div(b(name,":")," ",value)
    }
    //div(userInfo.mkString("|"))
  }

  /** User form HTML display */
  private def userForm:TypedTag[String] = {
    form(`class` := "needs-validation", action := "/admin/addPseudonyms", method := "POST", attr("novalidate") := "",
      div(`class` := "form-group text-right",
        input(`type` := "submit", value := "add pseuonyms", `class` := "btn btn-success")
      ),
      script(validationScript)
    )
  }

  private val validationScript = """
                                   |// Example starter JavaScript for disabling form submissions if there are invalid fields
                                   |          (function() {
                                   |          'use strict';
                                   |  window.addEventListener('load', function() {
                                   |    // Fetch all the forms we want to apply custom Bootstrap validation styles to
                                   |    var forms = document.getElementsByClassName('needs-validation');
                                   |    // Loop over them and prevent submission
                                   |    var validation = Array.prototype.filter.call(forms, function(form) {
                                   |      form.addEventListener('submit', function(event) {
                                   |        if (form.checkValidity() === false) {
                                   |          event.preventDefault();
                                   |          event.stopPropagation();
                                   |        }
                                   |        form.classList.add('was-validated');
                                   |      }, false);
                                   |    });
                                   |  }, false);
                                   |  })();
  """.stripMargin


}

/*
card("...",
              div( id := "reflection-points-contour-chart", `class` := "svg-container")
            )
 */

//  div(
//    div(`class`:="form-group",
//      label(attr("for"):="add-group","Enter a group to add..."),
//      input(`class`:="form-control", id := "add-group", name := "group"),
//    ),
//    div(`class`:="form-group",
//      input(`type`:="submit", value:="add", `class`:="btn btn-success")
//    )
//  )





//  sealed trait TableValues
//  case class IntTable(values:Seq[(String,Int,Int)]) extends TableValues
//  case class TypedTagStringTable(values:Seq[(String,TypedTag[String],TypedTag[String])]) extends TableValues
//
//  private def basicTable(tableVals:TableValues,headings:List[String]): TypedTag[String] = {
//
//    table(`class`:="table table-striped table-borderless table-sm",
//      thead(`class`:="thead-dark",
//        tr(th(attr("scope"):="col",headings(0)),th(attr("scope"):="col",headings(1)),th(attr("scope"):="col",headings(2)))
//      ),
//      tableVals match {
//        case IntTable(tVals) => tbody(
//          for((label,val1,val2) <- tVals) yield tr(style:="font-family:monospace;",td(label),td(val1),td(val2)),
//          tr(style:="font-family:monospace;",td(b("Total")),td(b(tVals.map(_._2).sum)),td(b(tVals.map(_._3).sum)))
//        )
//        case TypedTagStringTable(tVals) => tbody(
//          for((label,val1,val2) <- tVals) yield tr(style:="font-family:monospace;",td(label),td(val1),td(val2))
//        )
//        case _ => tbody()
//      }
//    )
//  }

