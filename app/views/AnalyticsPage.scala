package views

import java.time.LocalDate
import org.goingok.server.Config
import org.goingok.server.data.models.User
import org.goingok.server.data.{Analytics, UiMessage, models}
import scalatags.Text.all._
import scalatags.Text.tags
import views.components.NavBar.NavParams
import views.components._
import views.components.analytics.{Charts, SidePanel, Tables, TopPanel}
// scalastyle:ignore



class AnalyticsPage(user:Option[User]=None, analytics: Analytics, tester: Boolean, exp: Boolean) extends GenericPage {

  val title = "GoingOK :: analytics"

  /** Analytics page HTML display */
  def pageContent(titleStr: String,message:Option[UiMessage]): PageContent = {



    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user,Config.baseUrl,Some("analytics"))),
        div(`class`:="wrapper",
          if (tester){
            SidePanel.display(exp)
          } else {},
          div(id :="content", `class`:="content",
              div(`class`:="content-wrapper",
                div(id := "analytics-content",`class` := "container-fluid",
                  showMessage(message),
                  if (tester){
                    Charts.display()
                  } else {
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
                  },
                )
              )
          )
        ),
        script(src:=bundleUrl),
        if (tester){createChart(analytics.charts)}else{}
      )
    )
  }
  /** Creates analytics charts */
  private def createChart(data:Seq[models.AnalyticsChartsData]) = {
    val chartData:List[ujson.Obj] = data.toList.map(r => ujson.Obj("group" -> r.group,
      "value" -> r.value.map(c =>
        ujson.Obj("timestamp" -> c.timestamp, "pseudonym" -> c.pseudonym, "point" -> c.reflection.point, "text" -> c.reflection.text)),
      "createDate" -> r.timestamp))
    val entries:String = ujson.write(chartData)
    if (exp){
      script(raw(s"Visualisation.expAnalyticsCharts($entries)"))
    } else {
      script(raw(s"Visualisation.controlAnalyticsCharts($entries)"))
    }
  }
}

