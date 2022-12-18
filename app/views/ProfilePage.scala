package views


import org.goingok.server.Config
import org.goingok.server.data.models.{AnltxEdge, AnltxEdgeLabel, AnltxLabel, AnltxNode, AnltxNodeLabel, AuthorAnalytics, ReflectionEntry, UiMessage}
import org.goingok.server.data.AuthorProfile
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._
import views.components.profile.{MessagesList, Network, ReflectionEntryForm, ReflectionList, ReflectionPointChart, Reflections, Tags, Timeline}


class ProfilePage(authorProfile:AuthorProfile = AuthorProfile(), associateProfiles:Vector[AuthorProfile] = Vector(), flags:Map[String,Boolean] = Map() ) extends GenericPage {

  //the following are needed for transition from old ProfilePage
  private val tester = flags.getOrElse("tester",false)
  private val mcmexperiment = flags.getOrElse("mcm-experiment",false)

//class ProfilePage(profile:Profile = Profile(), analytics: Option[Map[String, AuthorAnalytics]], tester: Boolean, exp: Boolean) extends GenericPage {

  private val sliderStartPoint:Double = 50.0

  val title = "GoingOK :: profile"
  /** Displays HTML profile page */
  def pageContent(titleStr: String = this.title,message:Option[UiMessage]): TypedTag[String] = {

    //profile.user.map(u => (u.group_code+"_"+u.pseudonym))

    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(authorProfile.author,Config.baseUrl,Some("profile"))),
        div(`class`:="wrapper",
          if (tester) {
            div(id := "content", `class` := "content",
              div(`class` := "content-wrapper",
                div(`class` := "container-fluid",
                  div(id := "analytics-charts", `class` := "row",
                    div(`class` := "col-md-12 mt-3",
                      div(`class` := "row",
                        div(`class` := "col-md-4",
                          Includes.panel("reflection-entry", "fas fa-edit", "Enter a reflection", ReflectionEntryForm.display(sliderStartPoint),
                            "Use the slider to indicate how you are going from 'distressed' to 'soaring', " +
                              "and in the text box below write anything you like to describe how you how you are going. " +
                              "When finished, click the 'save' button to record your reflection.")
                        ),
                        div(id := "timeline", `class` := "col-md-8", Timeline.display()))),
                    div(`class` := "col-md-12 mt-3", Tags.display()),
                    div(`class` := "col-md-12 mt-3",
                      div(`class` := "row",
                        div(id := "reflections", `class` := "col-md-4", Reflections.display()),
                        div(id := "network", `class` := "col-md-8", Network.display())
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
                  Includes.panel("reflection-list", "fas fa-list-alt", "Past reflections", ReflectionList.display(authorProfile.analytics),
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
        createChart(authorProfile, associateProfiles)
      )
    )
  }

  /** Creates user chart */
  private def createChart(authorProfile:AuthorProfile, associateProfiles:Vector[AuthorProfile]) = {
    val authorRefs = parseReflections(authorProfile.analytics.map(_.refs).getOrElse(Vector()))
    val profiles = Vector(authorProfile) ++ associateProfiles
    val profilesReflectionJson = profiles.map(p => {
      ujson.Obj("pseudonym" -> p.author.flatMap(_.pseudonym).getOrElse[String](""),
        "reflections" -> parseReflections(p.analytics.map(_.refs).getOrElse(Vector()))
      )
    })
    val prj = ujson.write(profilesReflectionJson)
    logger.warn(s"Profile page arj: $prj")

    if (tester) {
      val profilesAnalyticsJson = profiles.map(p => {
        ujson.Obj("pseudonym" -> p.author.flatMap(_.pseudonym).getOrElse[String](""),
          "analytics" -> p.analytics.map(parseAnalytics).getOrElse(ujson.Obj())
        )
      })
      val paj = ujson.write(profilesAnalyticsJson)
      logger.warn(s"Profile page aaj: $paj")
      if (mcmexperiment){
        script(raw(s"gokd3.buildExperimentAuthorAnalyticsCharts($prj, $paj)"))
      } else {
        script(raw(s"gokd3.buildControlAuthorAnalyticsCharts($prj, $paj)"))
      }
    } else {
      val entries: String = ujson.write(authorRefs)
      script(raw(s"Visualisation.rpChart($entries)"))
    }
  }

  private def parseAnalytics(authorAnalytics: AuthorAnalytics): ujson.Obj = {
    authorAnalytics.charts.map(ch => {
      ujson.Obj("name" -> ch.name,
        "description" -> ch.description,
        "nodes" -> parseNodes(authorAnalytics.nodeLabels, authorAnalytics.nodes, authorAnalytics.labels),
        "edges" -> parseEdges(authorAnalytics.edgeLabels, authorAnalytics.edges, authorAnalytics.labels)
      )
    }).toList.headOption.getOrElse(ujson.Obj("name" -> "not found",
      "description" -> "not found",
      "nodes" -> parseNodes(Vector(), Vector(), Vector()),
      "edges" -> parseEdges(Vector(), Vector(), Vector())
    ))
  }

  private def parseNodes(nodeLabels: Vector[AnltxNodeLabel], nodes: Vector[AnltxNode], labels: Vector[AnltxLabel]): List[ujson.Obj] = {
    nodeLabels.map(nl => {
      val node = nodes.find(n => n.node_id == nl.node_id).getOrElse(AnltxNode(0, "", 0, 0, 0))
      val label = labels.find(l => l.label_id == nl.label_id).getOrElse(AnltxLabel(0,"","","",true,ujson.Obj()))
      ujson.Obj("idx" -> node.node_id,
      "nodeType" -> node.node_type,
      "refId" -> node.ref_id,
      "startIdx" -> node.start_idx,
      "endIdx" -> node.end_idx,
      "expression" -> nl.expression,
      "labelType" -> label.label_type,
      "name" -> label.ui_name,
      "description" -> label.description,
      "selected" -> label.selected,
      "properties" -> label.properties
    )}).toList
  }

  private def parseEdges(edgeLabels: Vector[AnltxEdgeLabel], edges: Vector[AnltxEdge], labels: Vector[AnltxLabel]): List[ujson.Obj] = {
    edgeLabels.map(el => {
      val edge = edges.find(e => e.edge_id == el.edge_id).getOrElse(AnltxEdge(0,0,"",0,0,false,0))
      val label = labels.find(l => l.label_id == el.label_id).getOrElse(AnltxLabel(0,"","","",true,ujson.Obj()))
      ujson.Obj("idx" -> edge.edge_id,
      "edgeType" -> edge.edge_type,
      "source" -> edge.source,
      "target" -> edge.target,
      "directional" -> edge.directional,
      "weight" -> edge.weight,
      "labelType" -> label.label_type,
      "name" -> label.ui_name,
      "description" -> label.description,
      "selected" -> label.selected,
      "properties" -> label.properties
    )}).toList
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

