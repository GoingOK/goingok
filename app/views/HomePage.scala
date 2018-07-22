package views

import org.goingok.server.Config
import org.goingok.server.data.models.User
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components.{HomeContent, NavBar}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HomePage extends GenericPage {

  override def page(titleStr: String, user: Option[User]=None,message:String=""): TypedTag[String] = {
    tags.html(
      attr("itemscope") := "",
      attr("itemtype") := "http://schema.org/Article",
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user.nonEmpty,displayName = None,baseUrl = Config.string("app.baseurl"),"home")),
        HomeContent.mainContainer,
        script(src:=bundleUrl)
      )
    )
  }

}
