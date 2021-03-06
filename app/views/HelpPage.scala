package views

import org.goingok.server.Config
import org.goingok.server.data.UiMessage
import org.goingok.server.data.models.User
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components.{HelpContent, NavBar}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
class HelpPage(user: Option[User]=None) extends GenericPage {


  val title = "GoingOK :: help"
  /** Displays help page */
  def pageContent(titleStr: String = this.title, message:Option[UiMessage]=None): PageContent = {
    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(user,Config.baseUrl,Some("help"))),
        HelpContent.mainContainer,
        script(src:=bundleUrl)
      )
    )
  }
}
