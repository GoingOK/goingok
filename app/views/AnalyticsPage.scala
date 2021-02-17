package views

import java.time.LocalDate
import org.goingok.server.Config
import org.goingok.server.data.models.User
import org.goingok.server.data.{Analytics, Profile, UiMessage, models}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._
// scalastyle:ignore



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
            div(`class`:="col", id:="groups",
              ac.groups
            ),
            div(`class`:="col",
              ac.reflectionsDownload
            ),
            div(`class`:="col", button(`class`:="d-none", id:="create-charts", "Visualise selected groups"), div()
//              card("...",
//                div()
//              )
            )
          ),
          div(id :="analytics-charts", `class`:="row")
        ),
        script(src:=bundleUrl),
        createChart(ac.charts.toVector)
      )
    )
  }
  /** Creates analytics charts */
  private def createChart(data:Vector[models.AnalyticsChartsData]) = {
    val chartData:List[ujson.Obj] = data.toList.map(r => ujson.Obj("group" -> r.group, "value" -> r.value.map(c => ujson.Obj("timestamp" -> c.timestamp, "pseudonym" -> c.pseudonym, "point" -> c.reflection.point))))
    val entries:String = ujson.write(chartData)
    script(raw(s"Visualisation.analyticsCharts($entries)"))
  }

}

