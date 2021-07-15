package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{_}

object SidePanel {
  def display(): TypedTag[String] = {
    div(id:="sidebar", `class`:= "active",
      div(`class`:="sidebar-header",
        h3("Your groups")
      ),
      ul(id:="groups", `class`:="list-unstyled components")
    )
  }
}
