package views.components.profile

import scalatags.Text.TypedTag
import scalatags.Text.all._

object ReflectionPointChart {

  def display() :TypedTag[String] = {

    div(
      div( id := "reflection-point-chart", `class` := "svg-container")
      //button(id:="click-me-button",`type`:="button", onclick:=s"org.goingok.client.Visualisation.rpChart($entries)")("Make chart")
    )
  }

}

