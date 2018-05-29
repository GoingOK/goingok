package views.components

import controllers.routes
import scalatags.Text.all._
import scalatags.Text.tags2

object NavBar {

  case class NavParams(signedIn:Boolean=false,displayName:Option[String]=None,baseUrl:String="http://localhost:9000",page:String="home")

  def main(navParams: NavParams = NavParams()) = {

    val linkDisabled = if(!navParams.signedIn) "disabled" else ""
    val profileActive = if(navParams.page=="profile") "active" else ""
    val displayName = navParams.displayName.getOrElse("")

    tag("nav")(name:="nav",id:="main-nav-bar",`class`:="navbar navbar-dark navbar-expand-lg", role:="navigation")(
      a(`class`:="navbar-brand goingok-font",href:=s"${navParams.baseUrl}")(
        img(id:="logo", src:=routes.Assets.versioned("images/GoingOK_Logo_small_transparent.png").url)
      ),
      button(`class`:="navbar-toggler", `type`:="button",
        attr("data-toggle"):="collapse", attr("data-target"):="#navbarSupportedContent",
        attr("aria-controls"):="navbarSupportedContent", attr("aria-expanded"):="false", attr("aria-label"):="Toggle navigation")
      (
        span(`class`:="navbar-toggler-icon")()
      ),
      div(`class`:="collapse navbar-collapse", id:="navbarSupportedContent")(
        ul(id:="main-menu", `class`:="navbar-nav mr-auto")(
          li(`class`:="nav-item")(
            a(`class`:=s"nav-link $linkDisabled $profileActive",if(navParams.signedIn) { href:="/profile"} else "")("Profile")
          )
        ),
        ul(`class`:="nav navbar-nav mx-auto")(
          li(`class`:="nav-item")(
            if(navParams.signedIn) a(`class`:="nav-link disabled")(s"$displayName")
            else ""
          )
        ),
        ul(`class`:="nav navbar-nav navbar-right")(
          li(`class`:="nav-item")(
            if(navParams.signedIn) a(`class`:="nav-link",href:="/signout")("Sign Out")
            else a(`class`:="nav-link",href:="/signin")("Sign In")
          ),
          li(`class`:="nav-item")(
            a(`class`:="nav-link",href:="/help")(span(`class` := "fas fa-question-circle"))
          )
        )
      )

    )
  }
}



