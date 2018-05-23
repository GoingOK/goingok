package views

import controllers.routes
import scalatags.Text.TypedTag
import views.components.{NavBar, ProfileContent}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object ProfilePage extends GenericPage {

  import scalatags.Text.all._
  import scalatags.Text.tags2

  override def page(titleStr:String):TypedTag[String] = html(
    head(
      tags2.title(titleStr),
      meta(charset:="utf-8"),
      meta(name:="viewport", content:="width=device-width, initial-scale=1"),
      //meta(attr("http-equiv"):="Content-Security-Policy", content:="script-src https://use.fontawesome.com/releases/v5.0.10/js/all.js"),
      link(rel:="icon",`type`:="image/x-icon", href:=routes.Assets.versioned("images/favicon.ico").url),
      link(rel:="stylesheet",href:=routes.Assets.versioned("stylesheets/bootstrap.min.css").url),
      link(rel:="stylesheet",href:=routes.Assets.versioned("stylesheets/navbar.component.css").url)
      //Includes.fontAwesomeJS,
      //Includes.bootstrapCSS,
      //Includes.goingokNavbarCSS
    ),
    body(
      NavBar.main,
      ProfileContent.mainContainer,
      //Includes.jqueryJS,
      //Includes.popperJS,
      //Includes.bootstrapJS,
      //Includes.d3JS,
      //Scripts.goingok
      script(src:=routes.Assets.versioned("clientjs-fastopt-bundle.js").url)
    )
  )
}
