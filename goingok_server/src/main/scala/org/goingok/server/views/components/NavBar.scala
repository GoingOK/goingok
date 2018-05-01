package org.goingok.server.views.components

object NavBar {
  import scalatags.Text.all._
  import scalatags.Text.tags2
  import controllers.routes.Assets

  val main = tag("nav")(name:="nav",id:="main-nav-bar",`class`:="navbar navbar-dark navbar-expand-lg", role:="navigation")(
    a(`class`:="navbar-brand goingok-font",href:="#")(
      img(id:="logo", src:=Assets.at("images/GoingOK_Logo_small_transparent.png").url)
    ),
    button(`class`:="navbar-toggler", `type`:="button",
      attr("data-toggle"):="collapse", attr("data-target"):="#navbarSupportedContent",
      attr("aria-controls"):="navbarSupportedContent", attr("aria-expanded"):="false", attr("aria-label"):="Toggle navigation")
    (
      span(`class`:="navbar-toggler-icon")()
    ),
    div(`class`:="collapse navbar-collapse", id:="navbarSupportedContent")(
      ul(id:="main-menu", `class`:="navbar-nav mr-auto")(
        li(`class`:="nav-item active")(a(`class`:="nav-link",href:="#")("About")),
        li(`class`:="nav-item")(a(`class`:="nav-link",href:="#")("Profile")),
        //li(`class`:="nav-item")(a(`class`:="nav-link",href:="#",`class`:="label label-danger")("WARNING: GoingOK is unable to connect to the server and may not function correctly."))
      ),
      ul(`class`:="nav navbar-nav navbar-right")(
        li(`class`:="nav-item")(a(`class`:="nav-link",href:="#")("Sign Out")),
        li(`class`:="nav-item")(a(`class`:="nav-link",href:="#")("Sign In")),
        li(`class`:="nav-item")(a(`class`:="nav-link",href:="#")(span(`class`:="glyphicon glyphicon-question-sign")())),
      )
    )

  )
}



