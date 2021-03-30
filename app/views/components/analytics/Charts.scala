package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all.{`class`, div, id, _}

object Charts {

  def display(): TypedTag[String] = {
    div(id :="analytics-charts", `class`:="row mt-3")
  }
}
