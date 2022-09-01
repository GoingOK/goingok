package views


import org.goingok.server.Config
import org.goingok.server.data.{Profile, UiMessage, models}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components._
import views.components.profile.{MessagesList, Network, ReflectionEntry, ReflectionList, ReflectionPointChart, Reflections, Sort, Tags, Timeline}


class ProfilePage(profile:Profile = Profile(), tester: Boolean, exp: Boolean) extends GenericPage {

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
                        div(`class` := "col-md-12 mt-3",
                          Includes.panel("reflection-entry", "fas fa-edit", "Enter a reflection", ReflectionEntry.display(sliderStartPoint),
                            "Use the slider to indicate how you are going from 'distressed' to 'soaring', " +
                              "and in the text box below write anything you like to describe how you how you are going. " +
                              "When finished, click the 'save' button to record your reflection.")
                        ),
                        if (exp) {
                          div(id := "sort", `class` := "col-md-12 mt-3",
                            Sort.display()
                          )
                        } else {},
                        div(id := "reflections", `class` := "col-md-12 mt-3",
                          Reflections.display()
                        )
                      )
                    ),
                    div(`class` := "col-md-8 mt-3",
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
                  Includes.panel("reflection-entry","fas fa-edit","Enter a reflection",ReflectionEntry.display(sliderStartPoint),
                  "Use the slider to indicate how you are going from 'distressed' to 'soaring', "+
                    "and in the text box below write anything you like to describe how you how you are going. "+
                    "When finished, click the 'save' button to record your reflection."),
                  Includes.panel("reflection-list", "fas fa-list-alt", "Past reflections", ReflectionList.display(profile.reflections.map(_.reverse)),
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
        createChart(profile.reflections)
      )
    )
  }

  /** Creates user chart */
  private def createChart(data:Option[Vector[models.ReflectionEntry]]) = {
    val refs = data.getOrElse(Vector()).toList
    val chartData:List[ujson.Obj] = refs.map(r => ujson.Obj("timestamp" -> r.bneDateTimeString, "point" -> r.reflection.point))
    val entries:String = ujson.write(chartData)
    script(raw(s"Visualisation.rpChart($entries)"))
  }
}

