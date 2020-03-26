package views.components

import controllers.routes
import org.goingok.server.data.models.User
import scalatags.Text.TypedTag
import scalatags.Text.all._ // scalastyle:ignore

object DemoNavBar {

  //case class NavParams(signedIn:Boolean=false,displayName:Option[String]=None,baseUrl:String="http://localhost:9000",page:String="home")

  case class NavParams(user:Option[User]=None,baseUrl:Option[String]=None,page:Option[String]=None)

  /** Renders demo navigation bar */
  def main(navParams: NavParams = NavParams()) :TypedTag[String] = {



    val homeUrl = navParams.baseUrl.getOrElse("http://goingok.org")



    /** Setting page to 'active' */
    def pageActive(thisPage:String):String = navParams.page match {
      case Some(page) if page.contentEquals(thisPage) => "active"
      case _ => ""
    }

    tag("nav")(name:="nav",id:="main-nav-bar",`class`:="navbar navbar-dark navbar-expand-lg")(

      // GoingOK Logo
      a(`class`:="navbar-brand goingok-font",href:=s"${homeUrl}")(
        img(id:="logo", src:=routes.Assets.versioned("images/GoingOK_Logo_small_transparent.png").url)
      ),

      // Collapsed Nav button
      button(`class`:="navbar-toggler", `type`:="button",
        attr("data-toggle"):="collapse",
        attr("data-target"):="#navbarSupportedContent",
        attr("aria-controls"):="navbarSupportedContent",
        attr("aria-expanded"):="false",
        attr("aria-label"):="Toggle navigation")
      (
        span(`class`:="fas fa-bars dark-blue-text")
      ),

      // Main Nav content
      div(`class`:="collapse navbar-collapse", id:="navbarSupportedContent")(

        // Right items
        ul(`class`:="nav navbar-nav ml-auto")(
          li(`class`:="nav-item")(
            a(`class`:=s"nav-link ${pageActive("help")}",href:="/help")(span(`class` := "fas fa-question-circle"))
          )
        )
      )





    )
  }


}



