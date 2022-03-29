package views.components.admin

import scalatags.Text.TypedTag
import scalatags.Text.all._
import views.GenericComponents

object Projects extends GenericComponents {

  def display(): TypedTag[String] = {
    card("Projects",
      div("Administer projects here...")
    )
  }
}
