package views.components.profile

import java.util.UUID

import org.goingok.server.data.models
import scalatags.Text.TypedTag
import scalatags.Text.all._

object ReflectionList {

  def display(data:Option[Vector[models.ReflectionEntry]]=None) :TypedTag[String] = {
    val refs = data.getOrElse(List())
    div(id:="reflection-box",
        for(ref <- refs) yield {
          div(`class` := "reflist-item",
            span(`class` := "reflist-item-point small badge badge-dark", s"${ref.reflection.point}"),
            span(`class` := "reflist-item-date small", b(s" ${ref.timestamp} ")),
            span(`class` := "reflist-item-text font-weight-light", s"${ref.reflection.text}")
          )
        },
      div(if(data.nonEmpty) { div(`class`:="text-center",span(`class`:="glyphicon glyphicon-chevron-down"))} else div())
    )
  }
}
