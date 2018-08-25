package org.goingok.server.services

import java.util.UUID

import com.typesafe.scalalogging.Logger
import org.goingok.server.data.DbResults
import org.goingok.server.data.Permissions.Permission

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

  def reflectionsForGroupCSV(group:String): Option[String] = ds.getReflectionsForGroup(group) match {
    case Right(result:DbResults.GroupedReflections) => {
      logger.info(s"reflections found: ${result.value.size}")
      val header = "TIMESTAMP,REF_POINT,REF_TEXT\n"
      val lines = result.value.map{re =>
        val text = re.reflection.text.replaceAll("\"","'").replaceAll("[\\n\\r]+"," ")
        s""""${re.timestamp}",${re.reflection.point},"${text}""""
      }.mkString("\n")
      Some(header+lines)
    }
    case Left(err) => {
      logger.error(err.getMessage)
      None
    }
  }

  def hasPermission(goingok_id:UUID,group_code:String,permission:Permission):Boolean = ds.getPermission(goingok_id,group_code) match {
    case Right(result: DbResults.Permission) => {
      logger.debug(s"received permission: ${result.value}")
      logger.debug(s"Checking match to: ${permission.value}")
      permission.matches(result.value)
    }
    case Left(err) => {
      logger.error(err.getMessage)
      false
    }
  }


}
