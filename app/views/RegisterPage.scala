package views


import models.User
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar
import views.components.NavBar.NavParams
import views.components.register.GroupPopup

object RegisterPage extends GenericPage {

  override def page(titleStr: String, user: Option[User]): TypedTag[String] = {
    val signedIn = user.nonEmpty
    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(signedIn,displayName = user.flatMap(_.fullName))),
        div(id := "register-content",`class` := "container-fluid",
          div(`class` := "row justify-content-md-center align-items-center",
            div(`class` := "col-sm-5",
              Includes.panel("registration","fas fa-id-card","Register with a group",GroupPopup.display())

            )
          )
        ),
        script(src:=bundleUrl)
        //Includes.startRegister
      )
    )
  }


}
