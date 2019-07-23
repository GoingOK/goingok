package views

import java.util.UUID

import org.goingok.server.Config
import org.goingok.server.data.{Profile, UiMessage, models}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._
import views.components.profile.{MessagesList, ReflectionEntry, ReflectionList, ReflectionPointChart}


object ProfilePage extends GenericPage {

  private val sliderStartPoint:Double = 50.0


  def page(titleStr: String,message:Option[UiMessage],profile:Profile = Profile()): TypedTag[String] = {

    //profile.user.map(u => (u.group_code+"_"+u.pseudonym))

    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(profile.user,Config.baseUrl,Some("profile"))),


        div(id := "profile-content",`class` := "container-fluid",
          showMessage(message),
          div( id := "reflectchart-content", `class` := "row",
            div( `class` := "col-sm-12",
              Includes.panel("reflection-points","fas fa-chart-line","GoingOK over time",ReflectionPointChart.display())
            )
          ),
          div( id := "main-content", `class` := "row",
            div( id := "main-left-column", `class` := "col-sm-8",
              Includes.panel("reflection-entry","fas fa-edit","Enter a reflection",ReflectionEntry.display(sliderStartPoint)),
              Includes.panel("reflection-list", "fas fa-list-alt", "Past reflections", ReflectionList.display(profile.reflections.map(_.reverse)))
            ),
            div( id := "main-right-column", `class` := "col-sm-4",
              Includes.panel("message-list", "fas fa-envelope", "Messages", MessagesList.display())
            )
          )
        ),
        //Includes.d3Js,
        script(src:=bundleUrl),
        createChart(profile.reflections)
      )
    )
  }

  private def createChart(data:Option[Vector[models.ReflectionEntry]]) = {
    val refs = data.getOrElse(Vector()).toList
    val chartData:List[ujson.Obj] = refs.map(r => ujson.Obj("timestamp" -> r.bneZonedDateTime.toOffsetDateTime.toString, "point" -> r.reflection.point))
    val entries:String = ujson.write(chartData)
    script(raw(s"org.goingok.client.Visualisation.rpChart($entries)"))
  }
}

