package views

import scalatags.Text.TypedTag
import scalatags.Text.all.{_}

trait GenericComponents {

  type CardComponent = TypedTag[String]
  type CardContent = TypedTag[String]

  /** Renders card data */
  def card(heading: String, content: TypedTag[String]): CardComponent = div(`class` := "card",
    h5(`class` := "card-header", heading),
    div(`class` := "card-body", content)
  )

}
