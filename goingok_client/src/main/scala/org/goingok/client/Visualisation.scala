package org.goingok.client

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("Visualisation")
object Visualisation {

  @JSExport
  /** Generates chart of user's reflections */
  def rpChart(entries:js.Array[js.Object]) :Unit = {
    println("Generating Visualisation: rpChart")
    //println(s"rpChart entries: $entries")
    RelfectionPointChart.buildChart(entries)
  }

  @JSExport
  /** Generates analytics page charts */
  def analyticsCharts(entries:js.Array[js.Object]) :Unit = {
    println("Generating Analytics Charts: analyticsCharts")
    AnalyticsCharts.buildAnalyticsCharts(entries)
  }
}
