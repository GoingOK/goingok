package org.goingok.client

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("org.goingok.client.Visualisation")
object Visualisation {

  @JSExport
  /** Generates chart of user's reflections */
  def rpChart(entries:js.Array[js.Object]) :Unit = {
    println("Generating Visualisation: rpChart")
    //println(s"rpChart entries: $entries")
    RelfectionPointChart.buildChart(entries)
  }
}
