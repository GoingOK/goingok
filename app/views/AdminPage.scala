package views

import java.time.LocalDate

import org.goingok.server.Config
import org.goingok.server.data.models.User
import org.goingok.server.data.{AdminData, Analytics, UiMessage, models}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._ // scalastyle:ignore


class AdminPage(user:Option[User]=None,adminData:AdminData) extends GenericPage {

  val title = "GoingOK :: admin"

  val ac = new AdminComponents(adminData)
  /** Admin page HTML display */
  def pageContent(titleStr: String = this.title,message:Option[UiMessage]): PageContent = {
    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user,Config.baseUrl,Some("admin"))),
        div(id := "analytics-content",`class` := "container-fluid",
          showMessage(message),
          div(`class`:="row",
            div(`class`:="col-4",
              ac.projectAdmin
            ),
            div(`class`:="col-5",
              ac.groupsAdmin
            ),
            div(`class`:="col",
              ac.usersAdmin
            ),
            div(`class`:="col-1",
              "..."
            )
          )
        ),
        script(src:=bundleUrl),
      )
    )
  }
}

