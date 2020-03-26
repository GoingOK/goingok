package views

import org.goingok.server.Config
import org.goingok.server.data.UiMessage
import org.goingok.server.data.models.User
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components.{HomeContent, NavBar}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
class HomePage(user: Option[User]=None) extends GenericPage {

  val title = "GoingOK :: home"

  /** Displays home page */
  def pageContent(titleStr: String = this.title, message:Option[UiMessage]=None): PageContent = {
    tags.html(
      attr("itemscope") := "",
      attr("itemtype") := "http://schema.org/Article",
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user,Config.baseUrl,Some("home"))),
        HomeContent.mainContainer,
        script(src:=bundleUrl)
      )
    )
  }

}
