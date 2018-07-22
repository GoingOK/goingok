package org.goingok.server.data.models

import java.util.UUID

case class User(
                 goingok_id:UUID,
                 pseudonym:Option[String]=None,
                 research_code:Option[String]=None,
                 research_consent:Boolean = false,
                 supervisor:Boolean = false,
                 admin:Boolean = false,
                 group_code:String = "none",
                 register_timestamp:Option[String]=None)

//
//import java.util.UUID
//
////import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
//
///**
// * The user object.
// *
// * @param userID The unique ID of the user.
// * @param loginInfo The linked login info.
// * @param firstName Maybe the first name of the authenticated user.
// * @param lastName Maybe the last name of the authenticated user.
// * @param fullName Maybe the full name of the authenticated user.
// * @param email Maybe the email of the authenticated provider.
// * @param avatarURL Maybe the avatar URL of the authenticated provider.
// * @param activated Indicates that the user has activated its registration.
// */

//case class User(
//  userID: UUID,
//  loginInfo: LoginInfo,
//  firstName: Option[String],
//  lastName: Option[String],
//  fullName: Option[String],
//  email: Option[String],
//  avatarURL: Option[String],
//  activated: Boolean) extends Identity {
//
//  /**
//   * Tries to construct a name.
//   *
//   * @return Maybe a name.
//   */
//  def name = fullName.orElse {
//    firstName -> lastName match {
//      case (Some(f), Some(l)) => Some(f + " " + l)
//      case (Some(f), None) => Some(f)
//      case (None, Some(l)) => Some(l)
//      case _ => None
//    }
//  }
//}
