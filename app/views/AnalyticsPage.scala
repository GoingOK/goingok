package views

import java.time.LocalDate
import org.goingok.server.Config
import org.goingok.server.data.models.{Author, GroupAnalytics, UiMessage, User}
import org.goingok.server.data.models
import scalatags.Text.all._
import scalatags.Text.tags
import views.components.NavBar.NavParams
import views.components._
import views.components.analytics.{Charts, SidePanel, Tables, TopPanel}
// scalastyle:ignore



class AnalyticsPage(user:Option[User]=None, analytics: GroupAnalytics, tester: Boolean, exp: Boolean) extends GenericPage {

  val title = "GoingOK :: analytics"

  // transition conversion of users to authors
  val author = user.map(new Author(_))

  /** Analytics page HTML display */
  def pageContent(titleStr: String,message:Option[UiMessage]): PageContent = {



    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(author,Config.baseUrl,Some("analytics"))),
        div(`class`:="wrapper",
          if (tester){
            SidePanel.display(exp)
          } else {},
          div(id :="content", `class`:="content",
              div(`class`:="content-wrapper",
                div(id := "analytics-content",`class` := "container-fluid",
                  showMessage(message),
                  if (tester){
                    Charts.display(exp)

                  } else {
                    downloadsSection(analytics)
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

  private def downloadsSection(analytics: GroupAnalytics) = {
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
  }

  /** Creates analytics charts */
  private def createChart(data:Seq[models.AnalyticsChartsData]) = {
    val chartData:List[ujson.Obj] = data.toList.map(r => ujson.Obj("group" -> r.group,
      "value" -> r.value.map(c =>
        ujson.Obj("refId" -> c.reflectionEntry.refId, "timestamp" -> c.reflectionEntry.timestamp, "pseudonym" -> c.pseudonym, "point" -> c.reflectionEntry.reflection.point, "text" -> c.reflectionEntry.reflection.text)),
      "createDate" -> r.timestamp))
    val entries:String = ujson.write(chartData)
    if (exp){
      script(raw(s"Visualisation.expAnalyticsCharts($entries)"))
    } else {
      script(raw(s"Visualisation.controlAnalyticsCharts($entries)"))
    }
  }
}

