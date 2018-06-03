package views

import controllers.routes
import scalatags.Text.{TypedTag, tags, tags2}

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
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/reflection-point-chart.css").url),
    script(src := routes.Assets.versioned("javascripts/fa-solid.min.js").url),
    script(src := routes.Assets.versioned("javascripts/fontawesome.min.js").url)
  )

  val clientJs = script(src := routes.Assets.versioned("javascripts/client-fastOptJS-bundle.js").url)
  val d3Js = script(src := routes.Assets.versioned("javascripts/d3.min.js").url)
  val startRegister = script("$('#register-modal').modal({ keyboard: false })")

  def panel(idName:String,icon:String,title:String,content:TypedTag[String]):TypedTag[String] = div(id := idName, `class` := "card profile-panel",
    div(`class` := "card-header",
      span(`class` := icon), b(s" $title"),
      span(`class` := "fas fa-question-circle float-right")
    ),
    div(`class` := "card-body",
      content
    )
  )

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
