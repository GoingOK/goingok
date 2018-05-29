package org.goingok.server.data.db


import java.util.UUID

import org.goingok.server.data.models.{Message, Reflection, User, UserAuth}
import org.goingok.server.data.db.postgresql.GoingokPostgresProfile.api._
import play.api.libs.json.JsValue
import slick.lifted.{TableQuery, Tag}



object Schema {

  class UserAuthSchema(tag: Tag) extends Table[UserAuth](tag, "user_auths") {
    def goingok_id = column[UUID]("goingok_id")

    def google_id = column[String]("google_id")

    def google_email = column[String]("google_email")

    def pkey = primaryKey("user_auth_pkey", (goingok_id))

    def * = (goingok_id, google_id, google_email) <> (UserAuth.tupled, UserAuth.unapply)
  }

  class UserSchema(tag: Tag) extends Table[User](tag, "users") {
    def goingok_id = column[UUID]("goingok_id")

    def pseudonym = column[String]("pseudonym")

    def research_code = column[String]("research_code")

    def research_consent = column[Boolean]("research_consent")

    def supervisor = column[Boolean]("supervisor")

    def admin = column[Boolean]("admin")

    def pkey = primaryKey("user_pkey", (goingok_id))

    def * = (goingok_id, pseudonym, research_code, research_consent, supervisor, admin) <> (User.tupled, User.unapply)
  }

//  implicit val jsValueMapper = MappedColumnType.base[JsValue, PGobject](
//    { json =>
//    {
//      val pgo = new PGobject()
//      pgo.setType("json")
//      pgo.setValue(Json.stringify(json))
//      pgo
//    }},
//    { pgo =>Json.parse(pgo.getValue) }
//  )
//  implicit val jsValueSetter = SetParameter[JsValue] {
//    case (jsvalue, params) => params.setString(Json.stringify(jsvalue))
//  }


  class MessageSchema(tag: Tag) extends Table[Message](tag, "messages") {



    def timestamp = column[String]("timestamp")

    def title = column[String]("title")

    def text = column[String]("text")

    def value = column[JsValue]("value")

    def goingok_id = column[UUID]("goingok_id")

    def * = (timestamp, title, text, value, goingok_id) <> (Message.tupled, Message.unapply)

    def user = foreignKey("user_fkey", goingok_id, TableQuery[UserSchema])(_.goingok_id)
  }

  class ReflectionSchema(tag: Tag) extends Table[Reflection](tag, "reflections") {
    def timestamp = column[String]("timestamp")

    def point = column[Double]("point")

    def text = column[String]("text")

    def goingok_id = column[UUID]("goingok_id")

    def * = (timestamp, point, text, goingok_id) <> (Reflection.tupled, Reflection.unapply)

    def user = foreignKey("user_fkey", goingok_id, TableQuery[UserSchema])(_.goingok_id)

  }

}
