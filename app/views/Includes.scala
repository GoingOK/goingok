package views

import controllers.routes
import scalatags.Text.{tags, tags2}

object Includes {
  import scalatags.Text.all._

  def headContent(titleStr:String) = tags.head(
    tags2.title(titleStr),
    meta(charset := "utf-8"),
    meta(name := "viewport", content := "width=device-width, initial-scale=1"),
    link(rel := "icon", `type` := "image/x-icon", href := routes.Assets.versioned("images/favicon.ico").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/bootstrap.min.css").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/main.css").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/navbar.component.css").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/fa-svg-with-js.css").url),
    script(src := routes.Assets.versioned("javascripts/fa-solid.min.js").url),
    script(src := routes.Assets.versioned("javascripts/fontawesome.min.js").url)
  )

  val clientJs = script(src := routes.Assets.versioned("clientjs-fastopt-bundle.js").url)

//  val bootstrapCSS = link(rel:="stylesheet",
//    href:="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css",
//    attr("integrity"):="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB",
//    attr("crossorigin"):="anonymous"
//  )
//
//  val jqueryJS = script(
//    src:="https://code.jquery.com/jquery-3.3.1.slim.min.js",
//    attr("integrity"):="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo",
//    attr("crossorigin"):="anonymous"
//  )
//
//  val popperJS = script(
//    src:="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js",
//    attr("integrity"):="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49",
//    attr("crossorigin"):="anonymous"
//  )
//
//  val bootstrapJS = script(
//    src:="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js",
//    attr("integrity"):="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T",
//    attr("crossorigin"):="anonymous"
//  )
//
//  val fontAwesomeJS = script(
//    src:="https://use.fontawesome.com/releases/v5.0.10/js/all.js",
//    attr("integrity"):="sha384-slN8GvtUJGnv6ca26v8EzVaR9DC58QEwsIk9q1QXdCU8Yu8ck/tL/5szYlBbqmS+",
//    attr("crossorigin"):="anonymous"
//  )
//
//  val d3JS = script(src:="https://d3js.org/d3.v5.min.js")

    //script(src:=Assets.at("js/d3.js").url)

//  val goingokNavbarCSS = link(href:=Assets.at("css/navbar.component.css").url, rel:="stylesheet")
//
//  val goingokJS = script(src:="./assets/goingok_client-fastopt.js")

  //link(href:=Assets.at("css/bootstrap.css").url, rel:="stylesheet"),
  //script(src:=Assets.at("js/bootstrap.js").url)

  //link(href:=Assets.at("css/reflectionChart.component.css").url, rel:="stylesheet"),
  //link(href:=Assets.at("css/profile.component.css").url, rel:="stylesheet"),
  //link(href:=Assets.at("css/entry.component.css").url, rel:="stylesheet"),
}
