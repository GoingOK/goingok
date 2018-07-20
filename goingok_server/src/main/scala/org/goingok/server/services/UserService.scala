package org.goingok.server.services

import java.util.UUID

import com.typesafe.scalalogging.Logger
import org.goingok.server.data.models.{GoogleUser, User, UserAuth}


class UserService {

  val logger = Logger(this.getClass)

  val ds = new DataService

  def getUser(googleUser:GoogleUser):Either[Throwable, User] = for {
      u1 <- ds.getUserWithGoogleId(googleUser.gId)
  } yield u1

  def createUser(googleUser:GoogleUser):Either[Throwable,User] = {
    logger.debug("Creating new user")
    val user = for {
      gid <- ds.insertNewUserAuth(new UserAuth(googleUser.gId, googleUser.email))
      u1 <- ds.insertNewUser(User(gid,Some("dummy")))
      u2 <- checkRegistration(u1)
    } yield u2
    logger.debug(s"user: $user")
    user
  }

  def isGroupCodeValid(code:String):Either[Throwable,Boolean] = for {
    gc <- ds.getGroupCode(code)
  } yield gc.group_code.contentEquals(code)


  def addGroupCodeToUser(code:String,goingok_id:UUID):Either[Throwable,Int] = for {
    rows <- ds.updateCodeForUser(code:String,goingok_id:UUID)
  } yield rows


  private def printAndReturn[A](message:A):A = { logger.debug(message.toString); message }

  private def checkRegistration(user:User) :Either[Throwable,User] = if(user.group_code.isEmpty || user.group_code.contains("none")) {
    Left(new Exception(UserService.NOT_REGISTERED_ERROR))
  } else {
    Right(user)
  }



    //    val userOpt = ds.getOrCreateUserWithGoogleId(googleUser.gId)
//    if(userOpt.isEmpty) {
//      logger.error(s"Unable to get or create user in database - id: ${googleUser.gId}")
//      Left(new Exception(UserService.USER_FETCH_ERROR))
//    } else {
//      val user = userOpt.get
//      if (user.group_code.isEmpty) {
//        //Redirect for registration
//        logger.warn("getRegisteredUser not implemented, so throwing an exception")
//        Left(new Exception(UserService.NOT_REGISTERED_ERROR))
//      } else {
//        //Insert login timestamp - profile
//        ds.insertLoginStage(user.goingok_id,"profile")
//        //Get reflections
//        ds.getReflectionsForUser(user.goingok_id).foreach(re => logger.info(s"ReflectionEntry: $re"))
//        //Redirect to profile page
//      }
//
//
//    }




  def createUser(googleId:String):Option[User] = {
    //Create a new goingok user and return the created user
    None
  }

  def addRegistration = {

  }

}
object UserService {
  val USER_FETCH_ERROR = "Unable to get or create user in database"
  val NOT_REGISTERED_ERROR = "User is not registered with GoingOK"
}