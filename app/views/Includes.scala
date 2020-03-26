package views

import controllers.routes
import org.goingok.server.Config
import scalatags.Text.{TypedTag, tags, tags2}

object Includes {
  import scalatags.Text.all._ // scalastyle:ignore

  lazy val googleClientId = Config.stringOpt("google.client.id")

  /** creates HTML head content */
  def headContent(titleStr:String): TypedTag[String] = tags.head(
    tags2.title(titleStr),
    meta(charset := "utf-8"),
    meta(name := "viewport", content := "width=device-width, initial-scale=1"),
    link(rel := "icon", `type` := "image/x-icon", href := routes.Assets.versioned("images/favicon.ico").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/bootstrap.min.css").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/main.css").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/navbar.component.css").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/fa-svg-with-js.css").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/reflection-point-chart.css").url),
    link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/entry.component.css").url),
    script(src := "//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" ),
    script(src := routes.Assets.versioned("javascripts/bootstrap.bundle.min.js").url),
    script(src := routes.Assets.versioned("javascripts/fa-solid.min.js").url),
    script(src := routes.Assets.versioned("javascripts/fontawesome.min.js").url),
    script(src := "https://apis.google.com/js/client:platform.js?onload=startgapi", attr("async"):="", attr("defer"):=""),
    initGoogleAuth,
    initPopovers
  )

  val initGoogleAuth = script(raw(
    s"""
       |    function startgapi() {
       |    console.log("Starting Google API");
       |      gapi.load('auth2', function() {
       |        auth2 = gapi.auth2.init({
       |          client_id: '${googleClientId}',
       |          // Scopes to request in addition to 'profile' and 'email'
       |          //scope: 'additional_scope'
       |        });
       |      });
       |    }
       """.stripMargin))

  val initPopovers = script(raw("""$(function () {$('[data-toggle="popover"]').popover()})"""))
  val startRegister = script("$('#register-modal').modal({ keyboard: false })")


  /** Creates HTML dev element for panels */
 def panel(idName:String,icon:String,title:String,content:TypedTag[String],help:String="Sorry, no help here!"):TypedTag[String] = div(
    id := idName, `class` := "card profile-panel",
    div(`class` := "card-header",
      span(`class` := icon), b(s" $title"),
      helpPopover(help)
    ),
    div(`class` := "card-body",
      content
    )
  )

  def helpPopover(content:String):TypedTag[String] = button(`type`:="button",`class`:="btn float-right btn-light",
    attr("data-toggle") := "popover",
    //attr("title") := "Popover title",
    attr("data-content") := content,
    span(`class` := "fas fa-question-circle")
  )

}
