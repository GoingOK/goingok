package views

import org.goingok.server.Config
import org.goingok.server.data.models.User
import org.goingok.server.data.{Analytics, Profile, UiMessage}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._ // scalastyle:ignore


object AnalyticsPage extends GenericPage {

  def page(titleStr: String,message:Option[UiMessage],user:Option[User]=None,analytics:Analytics): TypedTag[String] = {

//    val name:Option[String] = for {
//      u <- user
//      gc = u.group_code
//      p <- u.pseudonym
//    } yield s"$p@$gc"



    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user,Config.baseUrl,Some("analytics"))),
        div(id := "analytics-content",`class` := "container-fluid",
          showMessage(message),
          div(`class`:="row",
            div(`class`:="col",
              card("Groups",basicTable(hideGroups(analytics.userCounts),List("Group Code","No. Users")))
            ),
            div(`class`:="col",
              card("Groups",basicTable(hideGroups(analytics.reflectionCounts),List("Group Code","No. Reflections")))
            ),
            div(`class`:="col",
              card("Download Reflections",
                div(
                  div("qut-ifn600-182 [",
                    a(href:="/analytics/csv?group=qut-ifn600-182","all"),",",
                    a(href:="#","last week"),
                    "]"),
                  div("qut-edn610-182 [",
                    a(href:="/analytics/csv?group=qut-edn610-182","all"),",",
                    a(href:="#","last week"),
                    "]"),
                  div("qut-eun122-182 [",
                    a(href:="/analytics/csv?group=qut-eun122-182","all"),",",
                    a(href:="#","last week"),
                    "]"),
                )
              )
            )
          )
        ),
        script(src:=bundleUrl)
      )
    )
  }

  private def hideGroups(input:Seq[(String,Int)]):Seq[(String,Int)] = {
    val hiddenGroups = List("qut-admin","qut-staff","none")
    input.filterNot(b => hiddenGroups.contains(b._1))
  }

  private def card(heading:String,content:TypedTag[String]) = div(`class`:="card",
    h5(`class`:="card-header",heading),
    div(`class`:="card-body",content)
  )


  private def basicTable(tablerows:Seq[(String,Int)],headings:List[String]) = {
    table(`class`:="table table-striped table-borderless table-sm",
      thead(`class`:="thead-dark",
        tr(th(attr("scope"):="col",headings(0)),th(attr("scope"):="col",headings(1)))
      ),
      tbody(
        for(row <- tablerows) yield tr(style:="font-family:monospace;",td(row._1),td(row._2)),
        tr(style:="font-family:monospace;",td(b("Total")),td(b(tablerows.map(_._2).sum)))
      )
    )
  }

}

