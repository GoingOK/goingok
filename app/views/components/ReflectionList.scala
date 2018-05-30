package views.components

import java.util.UUID

import org.goingok.server.data.models.{Reflection, ReflectionEntry}
import scalatags.Text.TypedTag
import scalatags.Text.all._

object ReflectionList {

  val dummyData = List(
    Reflection("2018-01-01",100.0,"This is a dummy reflection. Number 1.",UUID.randomUUID()),
    Reflection("2018-02-02",50.0,"This is a dummy reflection Number 2. This is a dummy reflection Number 2. This is a dummy reflection Number 2.",UUID.randomUUID()),
    Reflection("2018-03-03",0.0,"This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3.",UUID.randomUUID()),
    )

  def display(data:Option[List[Reflection]]=None) :TypedTag[String] = {
    val refs = data.getOrElse(dummyData)
    div(id:="reflection-box",
        for(ref <- refs) yield {
          div(`class` := "reflist-item",
            span(`class` := "reflist-item-point small badge badge-dark", s"${ref.point}"),
            span(`class` := "reflist-item-date small", b(s" ${ref.timestamp} ")),
            span(`class` := "reflist-item-text font-weight-light", s"${ref.text}")
          )
        },
      div(if(data.nonEmpty) { div(`class`:="text-center",span(`class`:="glyphicon glyphicon-chevron-down"))} else div())
    )
  }
}
