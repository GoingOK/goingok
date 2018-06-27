package views

import java.util.UUID

import auth.data.User
import org.goingok.server.data.models.Reflection
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._
import views.components.profile.{MessagesList, ReflectionEntry, ReflectionList, ReflectionPointChart}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object ProfilePage extends GenericPage {

  private val dummyUid = UUID.randomUUID()
  private val dummyData = List(
    Reflection("2018-05-02T06:01:29.077Z",100.0,"This is a dummy reflection. Number 1.",dummyUid),
    Reflection("2018-06-03T06:01:29.077Z",50.0,"This is a dummy reflection Number 2. This is a dummy reflection Number 2. This is a dummy reflection Number 2.",dummyUid),
    Reflection("2018-08-04T06:01:29.077Z",0.0,"This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3.",dummyUid),
    Reflection("2018-08-20T06:01:29.077Z",70.0,"This is a dummy reflection. Number 4.",dummyUid),
  )
  private val sliderStartPoint:Double = 50.0


  override def page(titleStr: String, user: Option[User]): TypedTag[String] = {
    val signedIn = user.nonEmpty
    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(signedIn,displayName = user.get.fullName, page = "profile")),
        div(id := "profile-content",`class` := "container-fluid",
          div( id := "reflectchart-content", `class` := "row",
            div( `class` := "col-sm-12",
              Includes.panel("reflection-points","fas fa-chart-line","GoingOK over time",ReflectionPointChart.display())
            )
          ),
          div( id := "main-content", `class` := "row",
            div( id := "main-left-column", `class` := "col-sm-8",
              Includes.panel("reflection-entry","fas fa-edit","Enter a reflection",ReflectionEntry.display(sliderStartPoint)),
              Includes.panel("reflection-list", "fas fa-list-alt", "Past reflections", ReflectionList.display(Some(dummyData)))
            ),
            div( id := "main-right-column", `class` := "col-sm-4",
              Includes.panel("message-list", "fas fa-envelope", "Messages", MessagesList.display())
            )
          )
        ),
        //Includes.d3Js,
        script(src:=bundleUrl),
        createChart(dummyData)
      )
    )
  }


  private def createChart(data:List[Reflection]) = {
    val chartData:List[ujson.Js.Obj] = data.map(r => ujson.Js.Obj("timestamp" -> r.timestamp, "point" -> r.point))
    val entries:String = ujson.write(chartData)
    script(raw(s"org.goingok.client.Visualisation.rpChart($entries)"))
  }
}

