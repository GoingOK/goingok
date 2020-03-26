package org.goingok.server.services

import java.util.UUID

import com.typesafe.scalalogging.Logger
import org.goingok.server.data.models.{ReflectionData, ReflectionEntry, User}

class ProfileService {

  val logger = Logger(this.getClass)

  val ds = new DataService

  /**
    * Gets all reflections for a given user
    * @param goingok_id GoingOK user ID
    * @return Vector of reflections
    */
  def getReflections(goingok_id:UUID): Option[Vector[ReflectionEntry]] = {
    ds.getReflectionsForUser(goingok_id) match {
      case Right(refs) => Some(refs)
      case Left(error) => {
        logger.error(s"There was a problem getting reflections for $goingok_id: ${error.getMessage}")
        None
      }
    }
  }

  /**
    * Get GoingOK user from DB by GoingOK user ID
    * @param goingok_id GoingOK user ID
    * @return GoingOK user
    */
  def getUser(goingok_id:UUID): Option[User] = {
    ds.getUserForId(goingok_id) match {
      case Right(user) => Some(user)
      case Left(error) => {
        logger.error(s"There was a problem getting the user for $goingok_id: ${error.getMessage}")
        None
      }
    }
  }

  /**
    * Saves Reflection
    * @param reflection Reflection
    * @param goingok_id GoingOK user ID
    */
  def saveReflection(reflection:ReflectionData,goingok_id:UUID):Either[Throwable,Int] = for {
    rows <- ds.insertReflection(reflection:ReflectionData,goingok_id:UUID)
  } yield rows
}
