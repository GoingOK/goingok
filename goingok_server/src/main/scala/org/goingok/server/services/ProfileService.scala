package org.goingok.server.services

import java.util.UUID

import com.typesafe.scalalogging.Logger
import org.goingok.server.data.models.{ReflectionData, ReflectionEntry, User}

class ProfileService {

  val logger = Logger(this.getClass)

  val ds = new DataService

  def getReflections(goingok_id:UUID): Option[Vector[ReflectionEntry]] = {
    ds.getReflectionsForUser(goingok_id) match {
      case Right(refs) => Some(refs)
      case Left(error) => {
        logger.error(s"There was a problem getting reflections for $goingok_id: ${error.getMessage}")
        None
      }
    }
  }

  def getUser(goingok_id:UUID): Option[User] = {
    ds.getUserForId(goingok_id) match {
      case Right(user) => Some(user)
      case Left(error) => {
        logger.error(s"There was a problem getting the user for $goingok_id: ${error.getMessage}")
        None
      }
    }
  }

  def saveReflection(reflection:ReflectionData,goingok_id:UUID):Either[Throwable,Int] = for {
    rows <- ds.insertReflection(reflection:ReflectionData,goingok_id:UUID)
  } yield rows
}
