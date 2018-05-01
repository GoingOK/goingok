package org.goingok.server.views

import org.goingok.server.views.components.{ComponentTests, NavBar, ProfileContent}
import scalatags.Text.TypedTag


/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object ProfilePage extends GenericPage {

  import controllers.routes.Assets
  import scalatags.Text.all._
  import scalatags.Text.tags2

  override def page(titleStr:String):TypedTag[String] = html(
    head(
      tags2.title(titleStr),
      meta(charset:="utf-8"),
      meta(name:="viewport", content:="width=device-width, initial-scale=1"),
      //meta(attr("http-equiv"):="Content-Security-Policy", content:="script-src https://use.fontawesome.com/releases/v5.0.10/js/all.js"),
      link(rel:="icon",`type`:="image/x-icon", href:=Assets.at("images/favicon.ico").url),
      Includes.fontAwesomeJS,
      Includes.bootstrapCSS,
      Includes.goingokNavbarCSS
    ),
    body(
      NavBar.main,
      ProfileContent.mainContainer,
      Includes.jqueryJS,
      Includes.popperJS,
      Includes.bootstrapJS,
      Includes.d3JS,
      //Scripts.goingok
    )
  )
}