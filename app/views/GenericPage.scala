package views

import auth.data.User
import play.twirl.api.Html
import scalatags.Text.all._
import scalatags.Text.{tags, tags2}

trait GenericPage {

  def render(title:String,user:Option[User]=None) = Html("<!DOCTYPE html>" +page(title,user).render)

  def page(titleStr:String,user:Option[User]=None) = tags.html(head(tags2.title(titleStr)))

}