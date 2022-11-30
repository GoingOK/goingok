package views

import org.goingok.server.Config
import org.goingok.server.data.UiMessage
import org.goingok.server.data.models.{Author, User}
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
//import views.ProfilePage.{showMessage, sliderStartPoint}
import views.components.DemoNavBar.NavParams
import views.components.profile.{MessagesList, ReflectionEntryForm, ReflectionList, ReflectionPointChart}
import views.components.{DemoNavBar, HelpContent}
//import controllers.ProfileController.{dummyData}


class DemoPage(user: Option[User]=None) extends GenericPage {

  private val sliderStartPoint:Double = 50.0

  val title = "GoingOK :: demo"

  // transition from user to author
//  val author = user.map(new Author(_))

  /** Displays a DEMO home page */
  def pageContent(titleStr: String = this.title, message:Option[UiMessage]=None): PageContent = {
    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        DemoNavBar.main(NavParams(user,Config.baseUrl,Some("help"))),
        div(id := "profile-content",`class` := "container-fluid",
          div( id := "reflectchart-content", `class` := "row",
            div( `class` := "col-sm-12",
              h1("Demo"),
              p("You can test out the features of GOINGOK without setting up a database, No data entered here will be stored or logged. To run the production version please see the instructions at the bottom of the page."),
              Includes.panel("reflection-points","fas fa-chart-line","GoingOK over time",ReflectionPointChart.display())
            )
          ),
          div( id := "main-content", `class` := "row",
            div( id := "main-left-column", `class` := "col-sm-8",
              Includes.panel("reflection-entry","fas fa-edit","Enter a reflection",ReflectionEntryForm.demoDisplay(sliderStartPoint)),
              Includes.panel("reflection-list", "fas fa-list-alt", "Past reflections", ReflectionList.display())
            ),
            div( id := "main-right-column", `class` := "col-sm-4",
              Includes.panel("message-list", "fas fa-envelope", "Messages", MessagesList.display()),
              p("This project is in demo mode. To run the production version the following environment variables are required:"),

              if(Config.googleClientSecret.isDefined){
                p("GOOGLE_CLIENT_SECRET - Set")
              } else {
                p("GOOGLE_CLIENT_SECRET - Not Set")
              },

              if(Config.goingokSecret.isDefined){
                p("GOINGOK_SECRET - Set")
              } else {
                p("GOINGOK_SECRET - Not Set")
              },

              if(Config.googleRedirectUrl.isDefined){
                p("GOOGLE_REDIRECT_URL - Set")
              } else {
                p("GOOGLE_REDIRECT_URL - Not Set")
              },

              if(Config.dbUrl.isDefined){
                p("DB_URL - Set")
              } else {
                p("DB_URL - Not Set")
              },

              if(Config.googleClientId.isDefined){
                p("GOOGLE_CLIENT_ID - Set")
              } else {
                p("GOOGLE_CLIENT_ID - Not Set")
              },

              if(Config.dbUser.isDefined){
                p("DB_USER - Set")
              } else {
                p("DB_USER - Not Set")
              },

              if(Config.dbSecret.isDefined){
                p("DB_SECRET - Set")
              } else {
                p("DB_SECRET - Not Set")
              },

              p("For full instructions on setting environment variables, check the help page = http://docs.goingok.org/")

            )
          )
        ),
        script(src:=bundleUrl)
      )
    )
  }
}
