package org.goingok.server.services

import com.typesafe.scalalogging.Logger
import org.goingok.server.data.DbResults

class AnalyticsService {

  val logger = Logger(this.getClass)

  val ds = new DataService

  def groupedUserCounts: Option[Seq[(String,Int)]] = ds.countUsers match {
    case Right(result:DbResults.GroupedUserCounts) => {
      logger.info(s"user count result: ${result.value.toString}")
      Some(result.value)
    }
    case Left(err) => {
      logger.error(err.getMessage)
      None
    }
  }

  def groupedReflectionCounts: Option[Seq[(String,Int)]] = ds.countReflections match {
    case Right(result:DbResults.GroupedReflectionCounts) => {
      logger.info(s"reflection count result: ${result.value.toString}")
      Some(result.value)
    }
    case Left(err) => {
      logger.error(err.getMessage)
      None
    }
  }
}
