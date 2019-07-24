package org.goingok.client

import org.scalajs.dom._

import scala.scalajs.js.annotation.JSExportTopLevel

object ClientApp {

  def main(args: Array[String]): Unit = {

    println("ClientApp has loaded successfully.")

  }

  @JSExportTopLevel("rangestatic")
  var rangestatic = true

  @JSExportTopLevel("rangeChange")
  def rangeChange():Unit = {
    //println("change")
    document.getElementById("slider-group").setAttribute("style", "border: thin solid #00BB00;")
    rangestatic = false
  }

}

