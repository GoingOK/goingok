package views

import org.goingok.server.Config
import org.goingok.server.data.models.User
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar
import views.components.NavBar.NavParams
import views.components.register.GroupPopup

object RegisterPage extends GenericPage {

  override def page(titleStr: String, user: Option[User]=None,message:String=""): TypedTag[String] = {

    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user.nonEmpty,displayName = None,baseUrl = Config.string("app.baseurl"),"register")),
        div(id := "register-content",`class` := "container-fluid",
          div(`class` := "row justify-content-md-center align-items-center",
            div(`class` := "col-sm-5",
              Includes.panel("registration","fas fa-id-card","Register with a group",GroupPopup.display(message))

            )
          )
        ),
        script(src:=bundleUrl)
        //Includes.startRegister
      )
    )
  }


}
