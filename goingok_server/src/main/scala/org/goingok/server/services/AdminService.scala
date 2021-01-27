package org.goingok.server.services

import java.util.UUID

import com.typesafe.scalalogging.Logger
import io.nlytx.commons.privacy.Anonymiser
import org.goingok.server.data.DbResults
import org.goingok.server.data.models.{GroupCode, User}

/**
  * Handles all administrator functionality
  */
class AdminService {

  val logger: Logger = Logger(this.getClass)

  val ds = new DataService()

  /**
    * Gets all groups codes from DB
    * @return Sequence of group codes
    */

  def groupInfo: Seq[(String, Int)] = ds.getAllGroupCodes match {
    case Right(result:DbResults.GroupCodes) => {
      logger.info(s"Get groups result: ${result.value.toString}")
      result.value.map(g => (g.group_code,-1))
    }
    case Left(err) => {
      logger.error(err.getMessage)
      Seq((err.getMessage,-1))
    }
  }

  def groupAdminInfo: Seq[(String, String, String)] = ds.getGroupAdmins match {
    case Right(result:DbResults.GroupAdmins) => {
      logger.info(s"Get groupAmins result: ${result.value.toString}")
      result.value //.map(g => (g,-1))
    }
    case Left(err) => {
      logger.error(err.getMessage)
      Seq((err.getMessage,"",""))
    }
  }

  /**
    * Gets all users pseudonyms from DB
    * @return Sequence of pseudonyms
    */
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


  /**
    * Adds new group and inserts it into DB
    * @param groupCode user's group code
    * @param goingokId user's GoingOk ID
    */
  def addGroup(groupCode:String,goingokId:UUID): Either[Throwable,Int] = {
    for {
      g <- ds.insertGroup(groupCode,goingokId)
    } yield g
  }

  def addGroupAdmin(pseudonym:String,group:String,permission:String="SENSITIVE"): Either[Throwable,Int] = {
    for {
      i <- ds.getGoingokIdForPseudonym(pseudonym)
      g <- ds.insertGroupAdmin(i,group,permission)
    } yield g
  }

  /**
    * Creates new pseudonyms and inserts them into DB
    * @param num number of pseudonyms to be created
    */
  def createPseudonyms(num:Int): Either[Throwable,Int] = {
    for {
      ss <- ds.getAllPseudonyms.map(_.toSet)
      ns = Anonymiser.increaseBy(num,ss)
      dif = ns.diff(ss)
      res <- ds.insertPseudonyms(dif.toList)
    } yield res
  }

  /**
    * Checks if a user has admin role and returns the user if true
    * @param goingok_id GoingOk User ID
    * @return GoingOk Admin User
    */
  def getAdminUser(goingok_id:UUID): Either[Throwable, User] = {
    for {
      u <- ds.getUserForId(goingok_id)
      usr <- getAdminUser(u)
    } yield usr
  }


  /**
    * Helper to check if a user has admin role
    * @param user GoingOk user ID
    * @return GoingOk admin user
    */
  private def getAdminUser(user:User) = if(user.admin) {
    Right(user)
  } else {
    Left(new Exception("User is not an administrator."))
  }


}
