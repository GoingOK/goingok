package org.goingok.server.services

import java.time.{DayOfWeek, LocalDate}
import java.time.temporal.{Temporal, TemporalAdjusters}
import java.util.{Date, UUID}
import com.typesafe.scalalogging.Logger
import org.goingok.server.data.DbResults
import org.goingok.server.data.Permissions.Permission
import org.goingok.server.data.models.{AnalyticsChartsData, ReflectionAuthorEntry, ReflectionData}

import scala.collection.mutable.ListBuffer

class AnalyticsService {

  val logger = Logger(this.getClass)

  val ds = new DataService


  /**
    * Gets all existing group codes counts from DB
    * @return A sequence of group codes and counts
    */
  def groupedUserCounts: Option[Seq[(String,Int)]] = ds.countUsers match {
    case Right(result:DbResults.GroupedUserCounts) => {
      logger.info(s"user count result: ${result.value.toString}")

      Some(hideGroups(result.value))
    }
    case Left(err) => {
      logger.error(err.getMessage)
      None
    }
  }

  /**
    * Get GoingOK user from DB by GoingOK user ID
    * @return GoingOK user
    */
  def getTesters(): Vector[String] = {
    ds.getTesters() match {
      case Right(testers:DbResults.Testers) => testers.value.toVector
      case Left(error) => {
        logger.error(s"There was a problem getting a list of testers: ${error.getMessage}")
        Vector[String]()
      }
    }
  }

  /**
    * Gets all reflections counts from DB
    * @param goingok_id GoingOK user ID
    * @return A sequence of reflections and counts
    */
  def groupedReflectionCounts(goingok_id:UUID): Option[Seq[(String,Int)]] = ds.countReflections(goingok_id) match {
    case Right(result:DbResults.GroupedReflectionCounts) => {
      logger.info(s"reflection count result: ${result.value.toString}")
      Some(hideGroups(result.value))
    }
    case Left(err) => {
      logger.error(err.getMessage)
      None
    }
  }

  /**
    * Filters out specific groups
    * @param groupCounts Group counts
    */
  private def hideGroups(groupCounts:Seq[(String,Int)]):Seq[(String,Int)] = groupCounts.filterNot { case (group, count) =>
      group.contains("staff") || group.contains("admin") || group.contains("none")
  }

  /**
    * Creates a CSV of reflections for a given group
    * @param group GoingOK group code
    * @param range range
    * @return CSV representation of reflections
    */
  def reflectionsForGroupCSV(group:String,range:Option[String]=None): Option[String] = {
    val result  = if(range.isEmpty) {
      logger.info(s"getting all reflections for: $group")
      ds.getAuthorReflectionsForGroup(group) // all reflections
    } else {
      range match {
        case Some("week") =>
          logger.info(s"getting previous week's reflections for: $group")
          val now = LocalDate.now()
          val end = now.`with`(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
          val start = end.`with`(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
          logger.info(s"date range: $start - $end")
          ds.getAuthorReflectionsForGroupWithRange(group,start.toString,end.toString)
        case _ => Left(new Exception("Range is not valid"))
      }
    }
    result match {
      case Right(result:DbResults.GroupedAuthorReflections) => {
        logger.info(s"reflections found: ${result.value.size}")
        val header = "TIMESTAMP,AUTHOR,REF_POINT,REF_TEXT\n"
        val lines = result.value.map{re =>
          val text = re.reflection.text.replaceAll("\"","'").replaceAll("[\\n\\r]+"," ")
          s""""${re.timestamp}",${re.pseudonym},${re.reflection.point},"${text}""""
        }.mkString("\n")
        Some(header+lines)
      }
      case Left(err) => {
        logger.error(err.getMessage)
        None
      }
    }
  }

  /**
    * Checks if a user with the given GoingOK ID and GoingOK group code has permission to access the analytics service
    * @param goingok_id GoingOK user ID
    * @param group_code GoingOk group code
    * @param permission boolean permission
    * @return
    */
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

  def analyticsChartsData(goingok_id:UUID): Option[List[AnalyticsChartsData]] = {
    val result = ds.getAuthorReflectionsAndGroup(goingok_id)

    result match {
      case Right(result:DbResults.GroupedAuthorReflectionsByUser) => {
        logger.info(s"chart reflections found: ${result.value.size}")
        val analyticsChartsData = result.value.groupBy(c => c.group).map(c =>
          AnalyticsChartsData(c._1, c._2.map(r => ReflectionAuthorEntry(r.timestamp, r.pseudonym, ReflectionData(r.point, "")))))
        Some(analyticsChartsData.toList)
        }
      case Left(err) => {
        logger.error(err.getMessage)
        None
      }
    }
  }


}
