package views


import org.goingok.server.Config
import org.goingok.server.data.models.{AnalyticsAuthorChartsData, AnalyticsChartEdge, AnalyticsChartNode, AuthorAnalytics, ReflectionData, ReflectionEntry}
import org.goingok.server.data.{Profile, UiMessage, models}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._
import views.components.profile.{MessagesList, Network, ReflectionEntryForm, ReflectionList, ReflectionPointChart, Reflections, Sort, Tags, Timeline}



class ProfilePage(profile:Profile = Profile(), analytics: Option[Map[String, AuthorAnalytics]], tester: Boolean, exp: Boolean) extends GenericPage {

  private val sliderStartPoint:Double = 50.0

  val title = "GoingOK :: profile"
  /** Displays HTML profile page */
  def pageContent(titleStr: String = this.title,message:Option[UiMessage]): TypedTag[String] = {

    //profile.user.map(u => (u.group_code+"_"+u.pseudonym))

    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(profile.user,Config.baseUrl,Some("profile"))),
        div(`class`:="wrapper",
          if (tester) {
            div(id := "content", `class` := "content",
              div(`class` := "content-wrapper",
                div(`class` := "container-fluid",
                  div(id := "analytics-charts", `class` := "row",
                    div(`class` := "col-md-4 mt-3",
                      div(`class` := "row",
                        div(`class` := "col-md-12",
                          Includes.panel("reflection-entry", "fas fa-edit", "Enter a reflection", ReflectionEntryForm.display(sliderStartPoint),
                            "Use the slider to indicate how you are going from 'distressed' to 'soaring', " +
                              "and in the text box below write anything you like to describe how you how you are going. " +
                              "When finished, click the 'save' button to record your reflection.")
                        ),
                        if (exp) {
                          div(id := "sort", `class` := "col-md-12",
                            Sort.display()
                          )
                        } else {},
                        div(id := "reflections", `class` := "col-md-12 mt-3",
                          Reflections.display()
                        )
                      )
                    ),
                    div(`class` := "col-md-8",
                      div(`class` := "row",
                        div(id := "timeline", `class` := "col-md-12 mt-3",
                          Timeline.display()
                        ),
                        div(`class` := "col-md-12 mt-3",
                          Tags.display()
                        ),
                        div(id := "network", `class` := "col-md-12 mt-3",
                          Network.display()
                        )
                      )
                    )
                  )
                )
              )
            )
          } else {
            div(id := "profile-content",`class` := "container-fluid",
              showMessage(message),
              div( id := "reflectchart-content", `class` := "row",
                div( `class` := "col-sm-12",
                  Includes.panel("reflection-points","fas fa-chart-line","GoingOK over time",ReflectionPointChart.display(),
                  "Displays how you thought were going in the past. The purple dots are how you thought you were going at any point in time "+
                    "(from distressed to soaring). The red line shows a smoothed path (plotline) of your journey over time, "+
                    "and the yellow line shows your average reflection point.")
                )
              ),
              div( id := "main-content", `class` := "row",
                div( id := "main-left-column", `class` := "col-sm-8",
                  Includes.panel("reflection-entry","fas fa-edit","Enter a reflection",ReflectionEntryForm.display(sliderStartPoint),
                  "Use the slider to indicate how you are going from 'distressed' to 'soaring', "+
                    "and in the text box below write anything you like to describe how you how you are going. "+
                    "When finished, click the 'save' button to record your reflection."),
                  Includes.panel("reflection-list", "fas fa-list-alt", "Past reflections", ReflectionList.display(profile.reflections),
                  "A reverse ordered list of reflections that you have written to date. The number represents your reflection point from 0 to 100 "+
                    "where 0 is 'distressed' and 100 is 'soaring'."
                  )
                ),
                div( id := "main-right-column", `class` := "col-sm-4",
                  Includes.panel("message-list", "fas fa-envelope", "Messages", MessagesList.display(),
                  "Messages from GoingOK or from your group facilitator will appear here.")
                )
              )
            )
          }
        ),
        //Includes.d3Js,
        script(src:=bundleUrl),
        createChart(profile.reflections, analytics)
      )
    )
  }

  /** Creates user chart */
  //private def createChart(data:Option[Map[String,Vector[ReflectionEntry]]], analytics: Map[String, Vector[models.AnalyticsAuthorChartsData]]) = {
  private def createChart(data:Option[Map[String,Vector[ReflectionEntry]]], analytics: Option[Map[String, AuthorAnalytics]]) = {

    val uuidRefsRaw = data.getOrElse(Vector()).toList
    val uuidRefs = uuidRefsRaw.map(d =>
      ujson.Obj("pseudonym" -> d._1,
        "reflections" -> parseReflections(d._2)))
    val reflections = ujson.write(uuidRefs)
    if (tester) {
      val analyticsData = analytics.getOrElse(Map()).map { d =>
        ujson.Obj("pseudonym" -> d._1,
          "analytics" -> ujson.Obj("name" -> "to fix", //d._2.headOption.getOrElse(AnalyticsAuthorChartsData("", "", Vector(), Vector())).name,
            "description" -> "to fix", //d._2.headOption.getOrElse(AnalyticsAuthorChartsData("", "", Vector(), Vector())).description,
            "nodes" -> Vector(), //TO FIX //parseNodes(d._2.headOption.getOrElse(AnalyticsAuthorChartsData("", "", Vector(), Vector())).nodes),
            "edges" -> Vector() //TO FIX //parseEdges(d._2.headOption.getOrElse(AnalyticsAuthorChartsData("", "", Vector(), Vector())).edges)
          )
        )
      }
      //      val analyticsDataMultipleCharts: List[ujson.Obj] = analytics.map(r => ujson.Obj("name" -> r.name,
//        "description" -> r.description,
//        "nodes" -> parseNodes(r.nodes),
//        "edges" -> parseEdges(r.edges)
//      )).toList
      val analyticsEntries: String = ujson.write(analyticsData)
      if (exp){
        script(raw(s"Visualisation.authorExpAnalyticsCharts($reflections, $analyticsEntries)"))
      } else {
        script(raw(s"Visualisation.authorControlAnalyticsCharts($reflections, $analyticsEntries)"))
      }
    } else {
      val chartData: List[ujson.Obj] = parseReflections(uuidRefsRaw.headOption.getOrElse("noPseudonym" -> Vector(ReflectionEntry(0, "1900-01-01", ReflectionData(0, ""))))._2)
      val entries: String = ujson.write(chartData)
      script(raw(s"Visualisation.rpChart($entries)"))
    }
  }

  private def parseNodes(nodes: Vector[AnalyticsChartNode]): List[ujson.Obj] = {
    nodes.map(c => ujson.Obj("idx" -> c.id,
      "nodeType" -> c.nodeType,
      "refId" -> c.refId,
      "startIdx" -> c.startIdx,
      "endIdx" -> c.endIdx,
      "expression" -> c.expression,
      "labelType" -> c.labelType,
      "name" -> c.name,
      "description" -> c.description,
      "selected" -> c.selected,
      "properties" -> c.properties
    )).toList
  }

  private def parseEdges(edges: Vector[AnalyticsChartEdge]): List[ujson.Obj] = {
    edges.map(c => ujson.Obj("idx" -> c.id,
      "edgeType" -> c.edgeType,
      "source" -> c.source,
      "target" -> c.target,
      "directional" -> c.directional,
      "weight" -> c.weight,
      "labelType" -> c.labelType,
      "name" -> c.name,
      "description" -> c.description,
      "selected" -> c.selected,
      "properties" -> c.properties
    )).toList
  }

  private def parseReflections(refs: Vector[ReflectionEntry]): List[ujson.Obj] = {
    refs.map(r =>
      ujson.Obj("refId" -> r.refId,
        "timestamp" -> r.bneDateTimeString,
        "point" -> r.reflection.point,
        "text" -> r.reflection.text
      )).toList
  }
}

