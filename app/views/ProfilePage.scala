package views

import auth.data.User
import scalatags.Text.{TypedTag, tags}
import views.components.NavBar.NavParams
import views.components.{NavBar, ProfileContent}

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
object ProfilePage extends GenericPage {

  override def page(titleStr: String, user: Option[User]): TypedTag[String] = {
    val signedIn = user.nonEmpty
    tags.html(
      Includes.headContent(titleStr),
      tags.body(
        NavBar.main(NavParams(signedIn,displayName = user.get.fullName, page = "profile")),
        ProfileContent.mainContainer,
        Includes.clientJs
      )
    )
  }
}
