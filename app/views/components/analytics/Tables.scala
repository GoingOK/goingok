package views.components.analytics

import scalatags.Text.TypedTag
import scalatags.Text.all._

import java.time.LocalDate

object Tables {

  def groups(mergedCounts: Seq[(String,Int,Int)]): TypedTag[String] = {
     basicTable(IntTable(mergedCounts),List("Groups","Users","Reflections"))
  }

  def reflectionsDownload(mergedCounts: Seq[(String,Int,Int)]): TypedTag[String] = {
     basicTable(TypedTagStringTable(downloadLinks(mergedCounts)),List("Groups","all","last week"))
  }

  /** Creates a csv file of reflections data for download */
  private def downloadLinks(mergedCounts:Seq[(String,Int,Int)]):Seq[(String,TypedTag[String],TypedTag[String])] = {
    mergedCounts.map { case (group, uc, rc) =>
      val allLink = s"/analytics/csv?group=$group"
      val weekLink = allLink + "&range=week"
      val date = LocalDate.now().toString
      (group,a(href:=allLink,s"all ($rc)"),a(href:=weekLink,s"ending $date"))
    }
  }

  //  private def card(heading:String,content:TypedTag[String]) = div(`class`:="card",
  //    h5(`class`:="card-header",heading),
  //    div(`class`:="card-body",content)
  //  )


  sealed trait TableValues
  case class IntTable(values:Seq[(String,Int,Int)]) extends TableValues
  case class TypedTagStringTable(values:Seq[(String,TypedTag[String],TypedTag[String])]) extends TableValues

  /** Displays basic HTML table */
  private def basicTable(tableVals:TableValues,headings:List[String]): TypedTag[String] = {

    table(`class`:="table table-striped table-borderless table-sm",
      thead(`class`:="thead-dark",
        tr(th(attr("scope"):="col",headings(0)),th(attr("scope"):="col",headings(1)),th(attr("scope"):="col",headings(2)))
      ),
      tableVals match {
        case IntTable(tVals) => tbody(
          for((label,val1,val2) <- tVals) yield tr(style:="font-family:monospace;",td(label),td(val1),td(val2)),
          tr(style:="font-family:monospace;",td(b("Total")),td(b(tVals.map(_._2).sum)),td(b(tVals.map(_._3).sum)))
        )
        case TypedTagStringTable(tVals) => tbody(
          for((label,val1,val2) <- tVals) yield tr(style:="font-family:monospace;",td(label),td(val1),td(val2))
        )
        case _ => tbody()
      }
    )
  }
}
