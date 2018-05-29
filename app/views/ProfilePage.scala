package views

import auth.data.User
import controllers.routes
import scalatags.Text.TypedTag
import views.components.NavBar.NavParams
import views.components.{NavBar, ProfileContent}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object ProfilePage extends GenericPage {

  import scalatags.Text.all._
  import scalatags.Text.tags2

  override def page(titleStr: String, user: Option[User]): TypedTag[String] = {
    val signedIn = user.nonEmpty
    html(
      head(
        tags2.title(titleStr),
        meta(charset := "utf-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        //meta(attr("http-equiv"):="Content-Security-Policy", content:="script-src https://use.fontawesome.com/releases/v5.0.10/js/all.js"),
        //link(rel:="icon",`type`:="image/x-icon", href:=routes.Assets.versioned("images/favicon.ico").url),
        link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/bootstrap.min.css").url),
        link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/navbar.component.css").url),
        link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/fa-svg-with-js.css").url),
        script(src := routes.Assets.versioned("javascripts/fa-solid.min.js").url),
        script(src := routes.Assets.versioned("javascripts/fontawesome.min.js").url)
        //Includes.fontAwesomeJS,
        //Includes.bootstrapCSS,
        //Includes.goingokNavbarCSS
      ),
      body(
        NavBar.main(NavParams(signedIn,displayName = user.get.fullName, page = "profile")),
        ProfileContent.mainContainer,
        //Includes.jqueryJS,
        //Includes.popperJS,
        //Includes.bootstrapJS,
        //Includes.d3JS,
        //Scripts.goingok

        script(src := routes.Assets.versioned("clientjs-fastopt-bundle.js").url)
      )
    )
  }
}
