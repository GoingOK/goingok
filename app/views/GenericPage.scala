package views

import org.goingok.server.data.models.User
import play.twirl.api.Html
import scalatags.Text
import scalatags.Text.all._
import scalatags.Text.{tags, tags2}

trait GenericPage {

  def render(title:String,user:Option[User]=None,message:String="") :Html = Html("""<!DOCTYPE html>""" +page(title,user,message).render)

  def page(titleStr:String,user:Option[User]=None,message:String="") :Text.TypedTag[String] = tags.html(head(tags2.title(titleStr)))

  def bundleUrl: String = Seq("client-opt-bundle.js", "client-fastopt-bundle.js")
    .find(name => getClass.getResource(s"/public/$name") != null)
    .map(name => controllers.routes.Assets.versioned(s"$name").url).getOrElse("BUNDLE_NOT_FOUND")

}