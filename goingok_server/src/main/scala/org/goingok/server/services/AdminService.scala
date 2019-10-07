package org.goingok.server.services

import java.util.UUID

import com.typesafe.scalalogging.Logger
import io.nlytx.commons.privacy.Anonymiser
import org.goingok.server.data.DbResults
import org.goingok.server.data.models.{GroupCode, User}

/** Handles all data services for the admin page
 * == More Info ==
 * Some more detail about the [[org.goingok.server.services.AdminService]] class here.
 */
class AdminService {

  val logger: Logger = Logger(this.getClass)

  val ds = new DataService()

  /** groupInfo
   * Gets all group_codes from database and returns as sequence of (String,Int)
   * @return Seq[(String,Int)]
   */
  def groupInfo: Seq[(String, Int)] = ds.getAllGroupCodes match {
    case Right(result:DbResults.GroupCodes) => {
      logger.info(s"Get groups result: ${result.value.toString}")
      result.value.map(g => (g.toString,-1))
    }
    case Left(err) => {
      logger.error(err.getMessage)
      Seq((err.getMessage,-1))
    }
  }

  def userInfo: Seq[(String,Int)] = ds.getPseudonymCounts match {
    case Right(result:DbResults.GroupedPseudonymCounts) => {
      logger.info(s"Pseudonym count result: ${result.value.toString}")
      result.value.map { t =>
        val a = t._1 match {
          case Some(s) => if (s=="t") "allocated" else "not allocated"
          case None => "not allocated"
        }
        (a,t._2)
      }
    }
    case Left(err) => {
      logger.error(err.getMessage)
      Seq((err.getMessage,-1))
    }
  }


  def addGroup(groupCode:String,goingokId:UUID): Either[Throwable,Int] = {
    for {
      g <- ds.insertGroup(groupCode,goingokId)
    } yield g
  }


  def createPseudonyms(num:Int): Either[Throwable,Int] = {
    for {
      ss <- ds.getAllPseudonyms.map(_.toSet)
      ns = Anonymiser.generate(num,ss)
      dif = ns.diff(ss)
      res <- ds.insertPseudonyms(dif.toList)
    } yield res
  }

  def getAdminUser(goingok_id:UUID): Either[Throwable, User] = {
    for {
      u <- ds.getUserForId(goingok_id)
      usr <- getAdminUser(u)
    } yield usr
  }


  private def getAdminUser(user:User) = if(user.admin) {
    Right(user)
  } else {
    Left(new Exception("User is not an administrator."))
  }


}
