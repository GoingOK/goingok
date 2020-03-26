package views

import org.goingok.server.data.UiMessage
import org.goingok.server.data.models.User
import play.twirl.api.Html
import scalatags.Text
import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags, tags2}

trait GenericPage {

  type PageContent = TypedTag[String]

  def buildPage(title:String = this.title,message:Option[UiMessage]=None):Html = Html("""<!DOCTYPE html>""" +this.pageContent(title,message).render)

  def title:String

  def pageContent(titleStr: String = this.title, message:Option[UiMessage]=None):PageContent

  /** Gets the required HTML page */
  //def getHtml(content:PageContent):Html = Html("""<!DOCTYPE html>""" +content.render)

  /** Renders HTML page data */
  //def render(title:String,user:Option[User]=None,message:String="") :Html = Html("""<!DOCTYPE html>""" +page(title,user,message).render)

  /** Renders HTML page data */
  //def page(titleStr:String,user:Option[User],message:String) :Text.TypedTag[String] = tags.html(head(tags2.title(titleStr)))

  /** Bundles URL */
  def bundleUrl: String = Seq("client-opt-bundle.js", "client-fastopt-bundle.js")
    .find(name => getClass.getResource(s"/public/$name") != null)
    .map(name => controllers.routes.Assets.versioned(s"$name").url).getOrElse("BUNDLE_NOT_FOUND")

  /** Displays message */
  def showMessage(message:Option[UiMessage]): TypedTag[String] = message match {
    case Some(msg) => div(id:="message",`class`:=s"alert alert-${msg.style}",attr("role"):="alert",msg.text)
    case None => div()
  }

}