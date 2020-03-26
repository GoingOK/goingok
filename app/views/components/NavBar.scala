package views.components

import controllers.routes
import org.goingok.server.data.models.User
import scalatags.Text.TypedTag
import scalatags.Text.all._ // scalastyle:ignore

object NavBar {

  //case class NavParams(signedIn:Boolean=false,displayName:Option[String]=None,baseUrl:String="http://localhost:9000",page:String="home")

  case class NavParams(user:Option[User]=None,baseUrl:Option[String]=None,page:Option[String]=None)

  /** Renders navigation bar */
  def main(navParams: NavParams = NavParams()) :TypedTag[String] = {



    val isSignedIn:Boolean = navParams.user.nonEmpty
    val isCurator:Boolean = navParams.user match {
      case Some(user) => user.supervisor
      case None => false
    }
    val isAdministrator:Boolean = navParams.user match {
      case Some(user) => user.admin
      case None => false
    }
    val displayName:String = navParams.user match {
      case Some(user) => s"${user.pseudonym.getOrElse("noPseudonym")}@${user.group_code}"
      case None => ""
    }
    val linkDisabled:String = if(!isSignedIn) "disabled" else ""
    val analyticsDisabled:String = if(!(isSignedIn && isCurator)) "disabled" else ""
    val adminDisabled:String = if(!(isSignedIn && isAdministrator)) "disabled" else ""

    val homeUrl = navParams.baseUrl.getOrElse("http://goingok.org")


    /** Setting page to 'active' */
    def pageActive(thisPage:String):String = navParams.page match {
      case Some(page) if page.contentEquals(thisPage) => "active"
      case _ => ""
    }

    tag("nav")(name:="nav",id:="main-nav-bar",`class`:="navbar navbar-dark navbar-expand-lg")(

      // GoingOK Logo
      if(isSignedIn){
        a(`class`:="navbar-brand goingok-font",href:="/profile")(
          img(id:="logo", src:=routes.Assets.versioned("images/GoingOK_Logo_small_transparent.png").url)
        )
      } else {
        a(`class`:="navbar-brand goingok-font",href:=s"${homeUrl}")(
          img(id:="logo", src:=routes.Assets.versioned("images/GoingOK_Logo_small_transparent.png").url)
        )
      },

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
        // Left items
        ul(id:="main-menu", `class`:="nav navbar-nav mr-auto")(

          //Profile link - Only renders if logged in
          if(isSignedIn){
            li(`class`:="nav-item")(
              a(`class`:=s"nav-link $linkDisabled ${pageActive("profile")}",href:="/profile")("Profile")
            )
          } else {" "},

          // Analytics link - Only renders if signed in and is a curator
          if(isSignedIn && isCurator){
            li(`class`:="nav-item")(
              a(`class`:=s"nav-link $analyticsDisabled ${pageActive("analytics")}",href:="/analytics")("Analytics")
            )
          } else {" "},

          //Admin link - Only renders if logged in and is admin
          if(isSignedIn && isAdministrator) {
            li(`class`:="nav-item")(
              a(`class`:=s"nav-link $adminDisabled ${pageActive("admin")}",href:="/admin")("Admin")
            )
          } else {" "},
        ),
        // Centre items
        ul(`class`:="nav navbar-nav mx-auto")(
          li(`class`:="nav-item")(
            if(isSignedIn) { button(`class`:="btn btn-sm btn-outline-light", disabled)(displayName) } //(s"$displayName") }
            else { "" }
          )
        ),
        // Right items
        ul(`class`:="nav navbar-nav ml-auto")(
          li(`class`:="nav-item")(
            //button( id:="signinButton","Sign in with Google"),
            //            script(raw(
            //              """
            //                |$('#signinButton').click(function() {
            //                |    auth2.grantOfflineAccess().then(signInCallback);
            //                |  });
            //              """.stripMargin))
            if(isSignedIn) {
              a(`class`:="nav-link",href:="/signout")("Sign Out")
            } else {
              //a(`class`:="nav-link",onclick:="auth2.grantOfflineAccess().then(signInCallback)", href:="#")("Sign In")
              a(`class`:="nav-link", href:="/signin")("Sign In")
            }
          ),
          li(`class`:="nav-item")(
            a(`class`:=s"nav-link ${pageActive("help")}",href:="/help")(span(`class` := "fas fa-question-circle"))
          )
        )
      )





    )
  }


}



