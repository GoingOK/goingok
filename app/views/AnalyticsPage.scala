package views

import java.time.LocalDate
import org.goingok.server.Config
import org.goingok.server.data.models.{User}
import org.goingok.server.data.{Analytics, UiMessage, models}
import scalatags.Text.all._
import scalatags.Text.tags
import views.components.NavBar.NavParams
import views.components._
import views.components.analytics.{Charts, SidePanel, Tables}
// scalastyle:ignore



class AnalyticsPage(user:Option[User]=None, analytics: Analytics) extends GenericPage {

  val title = "GoingOK :: analytics"

  /** Analytics page HTML display */
  def pageContent(titleStr: String,message:Option[UiMessage]): PageContent = {



    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user,Config.baseUrl,Some("analytics"))),
        SidePanel.groups(analytics.charts.map(r => r.group).toList),
        SidePanel.btn(),
        div(id := "analytics-content",`class` := "container-fluid",
          showMessage(message),
          Charts.display(),
          div(`class`:="row mt-3",
            div(`class`:="col",
              Includes.panel("groups-table","","Groups", Tables.groups(analytics.mergedCounts))
            ),
            div(`class`:="col",
              Includes.panel("download-reflections-table", "","Download reflections", Tables.reflectionsDownload(analytics.mergedCounts))
            ),
            div(`class`:="col"
//              card("...",
//                div()
//              )
            )
          )
        ),
        script(src:=bundleUrl),
        createChart(analytics.charts)
      )
    )
  }
  /** Creates analytics charts */
  private def createChart(data:Seq[models.AnalyticsChartsData]) = {
    val chartData:List[ujson.Obj] = data.toList.map(r => ujson.Obj("group" -> r.group, "value" -> r.value.map(c => ujson.Obj("timestamp" -> c.timestamp, "pseudonym" -> c.pseudonym, "point" -> c.reflection.point))))
    val entries:String = ujson.write(chartData)
    script(raw(s"Visualisation.analyticsCharts($entries)"))
  }

}

