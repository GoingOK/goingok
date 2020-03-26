package views

import java.time.LocalDate

import org.goingok.server.Config
import org.goingok.server.data.models.User
import org.goingok.server.data.{Analytics, Profile, UiMessage}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._ // scalastyle:ignore



class AnalyticsPage(user:Option[User]=None,analytics:Analytics) extends GenericPage {

  val title = "GoingOK :: analytics"

  val ac = new AnalyticsComponents(analytics)

  /** Analytics page HTML display */
  def pageContent(titleStr: String,message:Option[UiMessage]): PageContent = {



    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user,Config.baseUrl,Some("analytics"))),
        div(id := "analytics-content",`class` := "container-fluid",
          showMessage(message),
          div(`class`:="row",
            div(`class`:="col",
              ac.groups
            ),
            div(`class`:="col",
              ac.reflectionsDownload
            ),
            div(`class`:="col", div()
//              card("...",
//                div()
//              )
            )
          )
        ),
        script(src:=bundleUrl)
      )
    )
  }


}

