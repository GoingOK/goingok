package org.goingok.server.services

import java.util.UUID
import javax.inject.Inject

import org.goingok.server.data.models.{User, UserAuth}
import org.goingok.server.data.db.DatabaseOps
import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by andrew@andrewresearch.net on 12/4/17.
  */
class UserService @Inject()(val dbOps:DatabaseOps)(implicit ec: ExecutionContext) {

  def getOrCreateGoingokId(google_id:String,google_email:String): Future[UUID] = {
    dbOps.selectUserAuthForGoogleId(google_id).map { uao =>
      if(uao.nonEmpty) {
        val gokId = uao.get.goingok_id
        Logger.info(s"Authorised user with goingok_id: $gokId")
        gokId
      }
      else {
        val newUserAuth = UserAuth(google_id=google_id,google_email=google_email)
        val newUser = User(newUserAuth.goingok_id,"","NON-NON-NON")
        Logger.info(s"Creating new user with goingok_id: ${newUserAuth.goingok_id}")
        dbOps.insertUserAuth(newUserAuth)
        dbOps.insertUser(newUser)
        newUserAuth.goingok_id
      }
    }
  }
}
