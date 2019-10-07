package views

import org.goingok.server.Config
import org.goingok.server.data.models.User
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components.{HelpContent, NavBar}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object HelpPage extends GenericPage {


  /** Displays help page */
  def page(titleStr: String, message:Option[String]=None,user: Option[User]=None): TypedTag[String] = {
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
