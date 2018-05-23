package views

import controllers.routes
import scalatags.Text.TypedTag
import views.components.{ComponentTests, NavBar}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HomePage extends GenericPage {

  import scalatags.Text.all._
  import scalatags.Text.tags2

  override def page(titleStr:String):TypedTag[String] = html(
    head(
      tags2.title(titleStr),
      meta(charset:="utf-8"),
      meta(name:="viewport", content:="width=device-width, initial-scale=1"),
      link(rel:="icon",`type`:="image/x-icon", href:=routes.Assets.versioned("images/favicon.ico").url),

      link(rel:="stylesheet",href:=routes.Assets.versioned("stylesheets/bootstrap.min.css").url),
      link(rel:="stylesheet",href:=routes.Assets.versioned("stylesheets/navbar.component.css").url)
    ),
    body(
      NavBar.main,
      ComponentTests.mainContainer,
      //Includes.bootstrapJS,
      //Includes.d3JS,
      //Includes.goingokJS
      script(src:=routes.Assets.versioned("clientjs-fastopt-bundle.js").url)
    )
  )
}
