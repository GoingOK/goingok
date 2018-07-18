package views

import models.User
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components.{HomeContent, NavBar}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HomePage extends GenericPage {

  override def page(titleStr: String, user: Option[User]=None): TypedTag[String] = {
    val signedIn = user.nonEmpty
    tags.html(
      attr("itemscope") := "",
      attr("itemtype") := "http://schema.org/Article",
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(signedIn,displayName = user.flatMap(_.fullName))),
        HomeContent.mainContainer,
        script(src:=bundleUrl)
      )
    )
  }

}
