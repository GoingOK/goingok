package views

import scalatags.Text.TypedTag

/**
  * Created by andrew@andrewresearch.net on 20/11/17.
  */
//object HomePageAngular extends GenericPage {
//
//  import scalatags.Text.all._
//  import scalatags.Text.tags2
//
//  override def page(titleStr:String):TypedTag[String] = html(
//    head(
//      tags2.title(titleStr),
//      meta(charset:="utf-8"),
//      meta(name:="viewport", content:="width=device-width, initial-scale=1"),
//      link(rel:="icon",`type`:="image/x-icon", href:=Assets.at("images/favicon.ico").url),
//      link(href:="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css", rel:="stylesheet"),
//      // Stylesheet bundle containing component styles for Angular app
//      link(rel:="stylesheet",href:=Assets.at("ui/styles.bundle.css").url)
//  ),
//    body(
//      tag("app-root")("Loading..."),
//      script(src:="https://apis.google.com/js/platform.js?onload=onLoadCallback"),
//      script(src:=Assets.at("ui/inline.bundle.js").url),
//      script(src:=Assets.at("ui/polyfills.bundle.js").url),
//      script(src:=Assets.at("ui/scripts.bundle.js").url),
//      script(src:=Assets.at("ui/main.bundle.js").url)
//    )
//  )
//}
