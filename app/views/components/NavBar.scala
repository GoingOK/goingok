package views.components

import controllers.routes
import scalatags.Text.TypedTag
import scalatags.Text.all._ // scalastyle:ignore

object NavBar {

  case class NavParams(signedIn:Boolean=false,displayName:Option[String]=None,baseUrl:String="http://localhost:9000",page:String="home")

  def main(navParams: NavParams = NavParams()) :TypedTag[String] = {

    val linkDisabled = if(!navParams.signedIn) "disabled" else ""
    val profileActive = if(navParams.page=="profile") "active" else ""
    val helpActive = if(navParams.page=="help") "active" else ""
    val displayName = navParams.displayName.getOrElse("")

    tag("nav")(name:="nav",id:="main-nav-bar",`class`:="navbar navbar-dark navbar-expand-lg")(
      // GoingOK Logo
      a(`class`:="navbar-brand goingok-font",href:=s"${navParams.baseUrl}")(
        img(id:="logo", src:=routes.Assets.versioned("images/GoingOK_Logo_small_transparent.png").url)
      ),
      // Collapsed Nav button
      button(`class`:="navbar-toggler", `type`:="button",
        attr("data-toggle"):="collapse", attr("data-target"):="#navbarSupportedContent",
        attr("aria-controls"):="navbarSupportedContent", attr("aria-expanded"):="false", attr("aria-label"):="Toggle navigation")
      (
        span(`class`:="fas fa-bars")
      ),
      // Main Nav content
      div(`class`:="collapse navbar-collapse", id:="navbarSupportedContent")(
        // Left items
        ul(id:="main-menu", `class`:="navbar-nav mr-auto")(
          li(`class`:="nav-item")(a(`class`:=s"nav-link $linkDisabled $profileActive",if(navParams.signedIn) { href:="/profile"} else "")("Profile"))
        ),
        // Centre items
        ul(`class`:="nav navbar-nav mx-auto")(
          li(`class`:="nav-item")(
            if(navParams.signedIn) { button(`class`:="btn btn-sm btn-outline-light", disabled)("qut182a-axe37wit") } //(s"$displayName") }
            else { "" }
          )
        ),
        // Right items
        ul(`class`:="nav navbar-nav ml-auto")(
          li(`class`:="nav-item")(
            if(navParams.signedIn) { a(`class`:="nav-link",href:="/signout")("Sign Out") }
            else { a(`class`:="nav-link",href:="/signin")("Sign In") }
          ),
          li(`class`:="nav-item")(
            a(`class`:=s"nav-link $helpActive",href:="/help")(span(`class` := "fas fa-question-circle"))
          )
        )
      )

    )
  }
}



