package org.goingok.client

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("./viz/index", JSImport.Namespace)
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

  /**
   * Builds authors analytics chart
   *
   * @param entries author reflections
   * @param analytics text analytics
   */
  def buildControlAuthorAnalyticsCharts(entries: js.Any, analytics: js.Any): js.Object = js.native

  /**
   * Builds authors analytics chart
   *
   * @param entries   author reflections
   * @param analytics text analytics
   */
  def buildExperimentAuthorAnalyticsCharts(entries: js.Any, analytics: js.Any): js.Object = js.native
}
