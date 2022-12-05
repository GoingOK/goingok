package org.goingok.client

import org.goingok.client.ReflectionPointChart.buildChart

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, JSGlobal}

@JSExportTopLevel("Visualisation")
object Visualisation {

  @JSExport
  /** Generates chart of user's reflections */
  def rpChart(entries:js.Array[js.Object]) :Unit = {
    println("Generating Visualisation: rpChart")
    //println(s"rpChart entries: $entries")
    buildChart(entries)
  }

  @JSExport
  /** Generates analytics page charts */
  def controlAnalyticsCharts(entries:js.Array[js.Object]) :Unit = {
    println("Generating Control Analytics Charts: controlAnalyticsCharts")
    AnalyticsCharts.buildControlAdminAnalyticsCharts(entries)
  }

  @JSExport
  /** Generates analytics page charts */
  def expAnalyticsCharts(entries:js.Array[js.Object]) :Unit = {
    println("Generating Experimental Analytics Charts: expAnalyticsCharts")
    AnalyticsCharts.buildExperimentAdminAnalyticsCharts(entries)
  }

  @JSExport
  /** Generates author control analytics page charts */
  def authorControlAnalyticsCharts(entries: js.Array[js.Object], analytics: js.Array[js.Object]): Unit = {
    println("Generating Author Control Analytics Charts: authorControlAnalyticsCharts")
    AnalyticsCharts.buildControlAuthorAnalyticsCharts(entries, analytics)
  }

  @JSExport
  /** Generates author control analytics page charts */
  def authorExpAnalyticsCharts(entries: js.Array[js.Object], analytics: js.Array[js.Object]): Unit = {
    println("Generating Author Control Analytics Charts: authorControlAnalyticsCharts")
    AnalyticsCharts.buildExperimentAuthorAnalyticsCharts(entries, analytics)
  }
}
