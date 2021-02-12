package views

import java.time.LocalDate
import org.goingok.server.Config
import org.goingok.server.data.models.User
import org.goingok.server.data.{Analytics, Profile, UiMessage, models}
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
          ),
          div(id :="analytics-charts", `class`:="row")
        ),
        script(src:=bundleUrl)
      )
    )
  }
  /** Creates analytics charts */
  private def createChart(data:Option[Vector[models.ReflectionEntry]]) = {
    val refs = data.getOrElse(Vector()).toList
    val chartData:List[ujson.Obj] = refs.map(r => ujson.Obj("timestamp" -> r.bneDateTimeString, "point" -> r.reflection.point))
    val entries:String = ujson.write(chartData)
    script(raw(s"Visualisation.rpChart($entries)"))
  }

}

