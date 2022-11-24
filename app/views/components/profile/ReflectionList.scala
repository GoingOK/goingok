package views.components.profile

import java.util.UUID

import org.goingok.server.data.models
import scalatags.Text.TypedTag
import scalatags.Text.all._

object ReflectionList {

  /** List of reflections HTML display */
  def display(data:Option[Map[String,Vector[models.ReflectionEntry]]]=None) :TypedTag[String] = {
    val refs = data.getOrElse(List()).head._2
    div(id:="reflection-box",
        for(ref <- refs) yield {
          div(`class` := "reflist-item",
            span(`class` := "reflist-item-point small badge badge-dark", "%02.0f".format(ref.reflection.point),
              style:=s"font-family:monospace; background-color: ${colour(ref.reflection.point.toInt)};"),
            span(`class` := "reflist-item-date small", b(s" ${ref.bneDateTimeString} ")),
            span(`class` := "reflist-item-text font-weight-light", s"${ref.reflection.text}")
          )
        },
      div(if(data.nonEmpty) { div(`class`:="text-center",span(`class`:="glyphicon glyphicon-chevron-down"))} else div())
    )
  }

  /** colour setting */
  def colour(num:Int): String = {
    val green = ((num/100.0)*220).toInt
    val blue = (((100-num)/100.0)*120).toInt
    s"rgb(0,$green,$blue)"
  }
}
