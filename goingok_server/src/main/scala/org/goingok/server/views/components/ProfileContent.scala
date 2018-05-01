package org.goingok.server.views.components

import scalatags.Text.TypedTag

object ProfileContent {
  import scalatags.Text.all._
  import scalatags.Text.tags2
  import controllers.routes.Assets

  lazy val mainContainer = div(id := "profile-content",`class` := "container-fluid",
    div( id := "reflectchart-content", `class` := "row",
      div( `class` := "col-sm-12",
        this.reflectChartPanel
      )
    ),
    div( id := "main-content", `class` := "row",
      div( id := "main-left-column", `class` := "col-sm-8",
        this.reflectEntryPanel,
        this.reflectListPanel
      ),
      div( id := "main-right-column", `class` := "col-sm-4",
        this.messageListPanel
      )
    )
  )

  lazy val reflectChartPanel:TypedTag[String] = panel("reflection-points","fas fa-chart-line","GoingOK over time",Charts.reflectionPoints)

  lazy val reflectEntryPanel:TypedTag[String] = panel("reflection-entry","fas fa-edit","Enter a reflection",this.entryForm)

  lazy val reflectListPanel:TypedTag[String] = panel("reflection-list", "fas fa-list-alt", "Past reflections", this.reflections)

  lazy val messageListPanel:TypedTag[String] = panel("message-list", "fas fa-envelope", "Messages", this.messages)

  def panel(idName:String,icon:String,title:String,content:TypedTag[String]):TypedTag[String] = div(id := idName, `class` := "card",
    div(`class` := "card-header",
      span(`class` := icon), b(s" $title"),
      span(`class` := "fas fa-question-circle float-right")
    ),
    div(`class` := "card-body",
      content
    )
  )



  lazy val entryForm = form( `class` := "form-horizontal", id := "form", name := "form", method := "post",
    div( `class` := "row",
      div( `class` :="col-xs-12",
        input( id := "point", name := "point", `type` := "hidden", value := "50" ),
        div( `class` := "top-spaced",
          this.slider
        )
      )
    ),
    div( `class` := "row",
      div( `class` := "col", "Distressed"),
      div( `class` := "col", "GoingOK"),
      div( `class` := "col", "Soaring")
    ),
    hr(),
    div( `class` := "row", div("Describe how you are going...")),
    div( `class` := "row", textarea( id := "describe", name := "describe", rows := "10", attr("ngModel") := "reflectText")),
    div( `class` := "row", button( `type` := "submit", `class` := "btn btn-success float-right", attr("click") := "saveEntry()","Save"))
  )

  lazy val slider = div(
    div( `class` := "top-spaced",
      div( id := "simple-slider", `class` := "dragdealer rounded-cornered",
        div( `class` :="red-bar handle",
          strong("Slider here")
        )
      )
    )
  )

  lazy val reflections = div("Reflections go here")

  lazy val messages = div("Messages go here")


//  lazy val reflectList = div( id := "reflection-list", `class` := "card",
//    div( `class` := "card-header",
//        span( `class` := "fas fa-list-alt"), b(" Past Reflections"),
//          span( `class` := "pull-right",
//            span( `class` := "header-button glyphicon glyphicon-download btn btn-default", attr("click") := "getCsv()"),
//            span( `class` := "header-button glyphicon glyphicon-question-sign btn btn-default", attr("popover") := "{{helptext}}", attr("placement") := "left")
//        )
//    ),
//    div( `class` := "card-body",
//      div( id := "reflection-box",
//        div( attr("ngIf") := "dataLoaded()",
//          div( attr("ngFor") := "let entry of getReflections()",
//
//          )
//        )
//      ),
//      div( `class` := "text-center",
//        span( `class` := "glyphicon glyphicon-chevron-down"),
//        span( `class` := "badge", "{{entry.reflection.point}}"), "&nbsp;&nbsp;", b("{{entry.timestamp | date:'dd MMM yyyy'}}"),"&nbsp;&nbsp;",
//        span("{{entry.reflection.text}}"),
//        hr()
//      )
//    )
//  )


//  lazy val messageList = div( id := "messages-list", `class` := "card",
//    div( `class` := "card-header",
//        span( `class` := "fas fa-envelope"), b(" GoingOK Messages"),
//        span( `class` := "header-button glyphicon glyphicon-question-sign pull-right btn btn-default", attr("popover") := "{{helptext}}", attr("placement") := "left"),
//    ),
//    div( id := "goingokMessage", `class` := "card-body small",
//      div( attr("ngIf") := "dataLoaded()",
//        div( attr("ngFor") := "let message of getMessages()",
//          p(strong("{{message.title}}"),"&nbsp;",span("[{{message.timestamp | date:'dd MMM yyyy'}}]")),
//          div(attr("innerHTML") := "message.text"),
//          div("{{message.value.toString()}}"),
//          hr()
//        )
//      )
//    ),
//    div( `class` := "text-center",
//      span( `class` := "glyphicon glyphicon-chevron-down")
//    )
//  )



}

