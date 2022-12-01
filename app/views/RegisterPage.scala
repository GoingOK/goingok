package views

import org.goingok.server.Config
import org.goingok.server.data.models.{Author, UiMessage, User}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar
import views.components.NavBar.NavParams
import views.components.register.GroupPopup

class RegisterPage(user: Option[User]=None) extends GenericPage {

  val title = "GoingOK :: Register"

  // transition from user to author
  val author = user.map(new Author(_))

  /** Displays register page */
  def pageContent(titleStr: String = this.title, message:Option[UiMessage]=None): PageContent = {

    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(author,Config.baseUrl,Some("register"))),
        div(id := "register-content",`class` := "container-fluid",
          div(`class` := "row justify-content-md-center align-items-center",
            div(`class` := "col-sm-5",
              Includes.panel("registration","fas fa-id-card","Register with a group",GroupPopup.display(this.showMessage(message)))

            )
          )
        ),
        script(src:=bundleUrl)
        //Includes.startRegister
      )
    )
  }



}
