package org.goingok.client

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("./analytics-charts", JSImport.Namespace)
@js.native
object AnalyticsCharts extends js.Object {

  /**
    * Builds analytics chart
    * @param entries reflections grouped by group code
    */
  def buildControlAdminAnalyticsCharts(entries:js.Any): js.Object = js.native

  /**
   * Builds analytics chart
   * @param entries reflections grouped by group code
   */
  def buildExperimentAdminAnalyticsCharts(entries:js.Any): js.Object = js.native
}
