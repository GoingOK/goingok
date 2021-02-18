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
        ac.selectGroups,
        div(`class`:="side-panel-button", button(`class`:="btn btn-dark", id:="side-panel-open-btn", i(`class`:="fa fa-chevron-right"))),
        div(id := "analytics-content",`class` := "container-fluid",
          showMessage(message),
          div(id :="analytics-charts", `class`:="row mt-3"),
          div(`class`:="row mt-3",
            div(`class`:="col",
              ac.groups
            ),
            div(`class`:="col",
              ac.reflectionsDownload
            ),
            div(`class`:="col"
//              card("...",
//                div()
//              )
            )
          )
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

