package org.goingok.server.services

import java.util.UUID
import com.typesafe.scalalogging.Logger
import org.goingok.server.data.models.{CognitoUser, User, UserAuth}


class UserService {

  val logger = Logger(this.getClass)

  val ds = new DataService

//  /**
//    * Gets GoingOK user from DB with google user info
//    * @param googleUser Google user
//    * @return GoingOK user
//    */
//  def getUser(googleUser:GoogleUser):Either[Throwable, User] = for {
//      u1 <- ds.getUserWithGoogleId(googleUser.gId)
//  } yield u1

  /**
    * Gets Goingok user from DB with cognito user info
    * @param cognitoUser Cognito user
    * @return GoingOK user
    */
  def getUser(cognitoUser:CognitoUser):Either[Throwable, User] = cognitoUser.googleId match {
    case Some(googleId) => for {
          u1 <- ds.getUserWithGoogleId(googleId)
      } yield u1
    case None => for {
      u1 <- ds.getUserWithServiceId(cognitoUser.id)
    } yield u1
  }



  /**
    * Gets GoingOK user from DB
    * @param goingok_id GoingOK user ID
    * @return GoingOK user
    */
  def getUser(goingok_id:UUID):Either[Throwable,User] = for {
    usr <- ds.getUserForId(goingok_id)
  } yield usr

//  /**
//    * Creates new GoingOK user
//    * @param googleUser Google user
//    */
//  def createUser(googleUser:GoogleUser):Either[Throwable,User] = {
//    logger.warn("Creating new user")
//    val user = for {
//      gid <- ds.insertNewUserAuth(new UserAuth(googleUser.gId, googleUser.email))
//      p <- getNewPseudonym
//      uuid <- ds.insertNewUser(User(gid,Some(p)))
//      usr <- ds.getUserForId(uuid)
//    } yield usr
//    logger.info(s"user: $user")
//    user
//  }

  /**
    * Creates new GoingOK user
    * @param googleUser Google user
    */
  def createUser(cognitoUser:CognitoUser):Either[Throwable,User] = {
    logger.warn("Creating new user")
    val user = for {
      gid <- ds.insertNewUserAuth(new UserAuth(cognitoUser.id))
      p <- getNewPseudonym
      uuid <- ds.insertNewUser(User(gid,Some(p)))
      usr <- ds.getUserForId(uuid)
    } yield usr
    logger.info(s"user: $user")
    user
  }

  /** Gets new pseudonym */
  def getNewPseudonym:Either[Throwable,String] = {
    logger.warn("Getting new pseudonym")
    for {
      p <- ds.getNextPseudonym
      i <- ds.updatePseudonym(p)
    } yield p
  }

  /**
    * Checks group code validity
    * @param code group code
    * @return Boolean
    */
  def isGroupCodeValid(code:String):Either[Throwable,Boolean] = for {
    gc <- ds.getGroupCode(code)
  } yield gc.group_code.toLowerCase.contentEquals(code.toLowerCase)


  /**
    * Adds group code to existing user
    * @param code group code
    * @param goingok_id GoingOK user ID
    */
  def addGroupCodeToUser(code:String,goingok_id:UUID):Either[Throwable,Int] = for {
    rows <- ds.updateCodeForUser(code:String,goingok_id:UUID)
  } yield rows

  private def printAndReturn[A](message:A):A = { logger.debug(message.toString); message }






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