package views

import scalatags.Text.all._
import scalatags.Text.{TypedTag, tags}

trait GenericComponents {

  type CardComponent = TypedTag[String]

  /** Renders card data */
  def card(heading:String,content:TypedTag[String]): CardComponent = div(`class`:="card",
    h5(`class`:="card-header",heading),
    div(`class`:="card-body",content)
  )

}
