package org.goingok.server.views

import org.goingok.server.views.fragments.NavBar

import scalatags.Text.TypedTag


/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HomePage extends GenericPage {

  import scalatags.Text.all._
  import scalatags.Text.tags2
  import controllers.routes.Assets

  override def page(titleStr:String):TypedTag[String] = html(
    head(
      tags2.title(titleStr),
      meta(charset:="utf-8"),
      meta(name:="viewport", content:="width=device-width, initial-scale=1"),
      link(rel:="icon",`type`:="image/x-icon", href:=Assets.at("images/favicon.ico").url),
      link(href:=Assets.at("css/bootstrap.css").url, rel:="stylesheet"),
      link(href:=Assets.at("css/navbar.component.css").url, rel:="stylesheet"),
    ),
    body(
      NavBar.main,
      p(id :="sjs","..."),
      div(id :="d3element"),
      script(src:=Assets.at("js/bootstrap.js").url),
      script(src:=Assets.at("js/d3.js").url),
      script(src:="./assets/goingok_client-fastopt.js")
    )
  )
}