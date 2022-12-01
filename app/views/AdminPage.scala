package views

import java.time.LocalDate
import org.goingok.server.Config
import org.goingok.server.data.models.{Author, UiMessage, User}
import org.goingok.server.data.{AdminData, models}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._
import views.components.admin.{Groups, Projects, Users} // scalastyle:ignore


class AdminPage(user:Option[User]=None,adminData:AdminData) extends GenericPage {

  val title = "GoingOK :: admin"

  // transition from user to author
  val author = user.map(new Author(_))

  /** Admin page HTML display */
  def pageContent(titleStr: String = this.title,message:Option[UiMessage]): PageContent = {
    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(author,Config.baseUrl,Some("admin"))),
        div(id := "analytics-content",`class` := "container-fluid",
          showMessage(message),
          div(`class`:="row",
            div(`class`:="col-4",
              Projects.display(adminData)
            ),
            div(`class`:="col-5",
              Groups.display(adminData)
            ),
            div(`class`:="col",
              Users.display(adminData)
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

