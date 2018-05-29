package views

import auth.data.User
import controllers.routes
import scalatags.Text.TypedTag
import scalatags.Text.all.link
import views.components.NavBar.NavParams
import views.components.{ComponentTests, NavBar}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HomePage extends GenericPage {

  import scalatags.Text.all._
  import scalatags.Text.tags2

  override def page(titleStr: String, user: Option[User]): TypedTag[String] = {
    val signedIn = user.nonEmpty
    html(
      head(
        tags2.title(titleStr),
        meta(charset := "utf-8"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1"),
        link(rel := "icon", `type` := "image/x-icon", href := routes.Assets.versioned("images/favicon.ico").url),
        link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/bootstrap.min.css").url),
        link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/navbar.component.css").url),
        link(rel := "stylesheet", href := routes.Assets.versioned("stylesheets/fa-svg-with-js.css").url),
        script(src := routes.Assets.versioned("javascripts/fa-solid.min.js").url),
        script(src := routes.Assets.versioned("javascripts/fontawesome.min.js").url)
      ),
      body(
        NavBar.main(NavParams(signedIn,displayName = user.flatMap(_.fullName))),
        ComponentTests.mainContainer,
        div(s"This is the home page. Signed in: $signedIn"),
        if(signedIn) {

          div(
            div(s"You are signed in as: ${user.get.fullName}"),
            div(a(href:=routes.ApplicationController.profile().url,"Profile page")),
            div(a(href:=routes.ApplicationController.signOut().url,"Sign out"))
          )
        } else {
          div(a(href:=routes.AuthController.authenticateWithGoogle().url,"Sign in with Google"))
        },
        script(src := routes.Assets.versioned("clientjs-fastopt-bundle.js").url)
      )
    )
  }
}
