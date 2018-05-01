package org.goingok.server.views.components

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
        this.reflectEntry,
        this.reflectList
      ),
      div( id := "main-right-column", `class` := "col-sm-4",
        this.messageList
      )
    )
  )

  lazy val reflectChartPanel = div(id := "reflection-points", `class` := "card",
    div(`class` := "card-header",
      //div(`class` := "panel-title",
      span(`class` := "fas fa-chart-line"), b(" GoingOK over time"),
      span(`class` := "header-button glyphicon glyphicon-question-sign pull-right btn btn-default",
        attr("popover") := "{{helptext}}", attr("placement") := "bottom")
      //)
    ),
    div(`class` := "card-body",
      Charts.reflectionPoints
    )
  )

  lazy val reflectEntry = div( `class` := "card",
    div( `class` := "card-header",
        span( `class` := "fas fa-edit"), b(" Enter a reflection"),
        span( `class` := "header-button fas fa-question-circle pull-right btn btn-default", attr("popover") := "{{helptext}}", attr("placement") := "right")
    ),
    div( `class` := "card-body",
      form( `class` := "form-horizontal", id := "form", name := "form", method := "post",
        div( `class` := "row",
          div( `class` :="col-xs-12",
            input( id := "point", name := "point", `type` := "hidden", value := "50" ),
            div( `class` := "top-spaced",
              this.slider
            )
          )
        ),
        div( `class` := "row",
          div( `class` := "col-xs-4",
            div( style := "text-align: left",
              h5("Distressed")
            )
          ),
          div( `class` := "col-xs-4",
            div( style := "text-align: center",
              h5("GoingOK")
            )
          ),
          div( `class` := "col-xs-4",
            div( style := "text-align: right",
              h5("Soaring")
            )
          )
        ),
        hr(),
        div( `class` := "row",
          div( `class` := "col-xs-12",
            h5("Describe how you are going..."),
            textarea( `class` := "col-xs-12", id := "describe", name := "describe", rows := "10", attr("ngModel") := "reflectText"),
            div("&nbsp;"),
            div(button( `type` := "submit", `class` := "btn btn-success pull-right", attr("click") := "saveEntry()","Save"))
          )
        )
      )
    )
  )

  lazy val slider = div(
    div( `class` := "top-spaced",
      div( id := "simple-slider", `class` := "dragdealer rounded-cornered",
        div( `class` :="red-bar handle",
          strong("&lt;&lt;&nbsp;&nbsp;&nbsp;&gt;&gt;")
        )
      )
    )
  )


  lazy val reflectList = div( id := "reflection-list", `class` := "card",
    div( `class` := "card-header",
        span( `class` := "fas fa-list-alt"), b(" Past Reflections"),
          span( `class` := "pull-right",
            span( `class` := "header-button glyphicon glyphicon-download btn btn-default", attr("click") := "getCsv()"),
            span( `class` := "header-button glyphicon glyphicon-question-sign btn btn-default", attr("popover") := "{{helptext}}", attr("placement") := "left")
        )
    ),
    div( `class` := "card-body",
      div( id := "reflection-box",
        div( attr("ngIf") := "dataLoaded()",
          div( attr("ngFor") := "let entry of getReflections()",

          )
        )
      ),
      div( `class` := "text-center",
        span( `class` := "glyphicon glyphicon-chevron-down"),
        span( `class` := "badge", "{{entry.reflection.point}}"), "&nbsp;&nbsp;", b("{{entry.timestamp | date:'dd MMM yyyy'}}"),"&nbsp;&nbsp;",
        span("{{entry.reflection.text}}"),
        hr()
      )
    )
  )


  lazy val messageList = div( id := "messages-list", `class` := "card",
    div( `class` := "card-header",
        span( `class` := "fas fa-envelope"), b(" GoingOK Messages"),
        span( `class` := "header-button glyphicon glyphicon-question-sign pull-right btn btn-default", attr("popover") := "{{helptext}}", attr("placement") := "left"),
    ),
    div( id := "goingokMessage", `class` := "card-body small",
      div( attr("ngIf") := "dataLoaded()",
        div( attr("ngFor") := "let message of getMessages()",
          p(strong("{{message.title}}"),"&nbsp;",span("[{{message.timestamp | date:'dd MMM yyyy'}}]")),
          div(attr("innerHTML") := "message.text"),
          div("{{message.value.toString()}}"),
          hr()
        )
      )
    ),
    div( `class` := "text-center",
      span( `class` := "glyphicon glyphicon-chevron-down")
    )
  )



}

