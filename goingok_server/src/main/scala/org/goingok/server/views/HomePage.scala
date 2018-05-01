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
      script(src:="https://code.jquery.com/jquery-3.2.1.slim.min.js"),
        //attr("integrity"):="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN",
        //attr("crossorigin"):="anonymous"),
      script(src:="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"),
        //attr("integrity"):="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q",
        //attr("crossorigin"):="anonymous"),
      script(src:=Assets.at("js/bootstrap.js").url),
      script(src:="./assets/goingok_client-fastopt.js")
    )
  )
}