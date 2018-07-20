package org.goingok.server.services

import java.time.LocalDateTime

import cats.effect._
import doobie._
import doobie.implicits._
import java.util.UUID

import doobie.util.meta.AdvancedMeta
import org.goingok.server.Config
import org.goingok.server.data.models.{GroupCode, ReflectionEntry, User, UserAuth}

class DataService {

  private val driver = Config.string("db.driver")
  private val url = Config.string("db.url")
  private val user = Config.string("db.user")
  private val password = Config.string("db.password")
  private val db = Transactor.fromDriverManager[IO](driver,url,user,password)

  private implicit val uuidImplicit:AdvancedMeta[UUID] =  doobie.postgres.implicits.UuidType

  def runQuery[A](query:ConnectionIO[A]):Either[Throwable,A] = query.transact(db).attempt.unsafeRunSync

  def getUserWithGoogleId(googleId:String):Either[Throwable,User] = {
    val query = sql"""select * from users u, user_auths a
                      where u.goingok_id = a.goingok_id
                      and a.google_id = $googleId
                   """.query[User].unique

    runQuery(query)
  }

  def insertNewUserAuth(userauth:UserAuth):Either[Throwable,UUID] = {
    val query = sql"""insert into user_auths (goingok_id, google_id, google_email, init_timestamp)
                      values(${userauth.goingok_id},${userauth.google_id},${userauth.google_email},${userauth.init_timestamp})
                   """.update
                  .withUniqueGeneratedKeys[UUID]("goingok_id")

    runQuery(query)
  }

  def insertNewUser(user:User):Either[Throwable,User] = {
    val query = sql"""insert into users (goingok_id,pseudonym,research_code,research_consent,supervisor,admin,group_code,register_timestamp)
                      values(${user.goingok_id},${user.pseudonym},
                            ${user.research_code},${user.research_consent},
                            ${user.supervisor},${user.admin},
                            ${user.group_code},${user.register_timestamp}
                            )
                   """.update
                  .withUniqueGeneratedKeys[UUID]("goingok_id")

    val result = runQuery(query)
    for {
      u <- result
      usr <- getUserForId(u)
    } yield usr
  }

  def getUserForId(goingok_id:UUID):Either[Throwable,User] = {
    val query = sql"""select * from users
                 where goingok_id = $goingok_id::uuid
              """.query[User].unique

    runQuery(query)
  }

  def getReflectionsForUser(goingokId:UUID): Either[Throwable,Vector[ReflectionEntry]] = {
    val query = sql"""select timestamp, point, text
                  from reflections
                  where goingok_id=$goingokId""".query[ReflectionEntry]

    runQuery(query.to[Vector])
  }


  def getGroupCode(code:String): Either[Throwable,GroupCode] = {
    val query = sql"""select * from group_codes
                      where group_code=$code""".query[GroupCode].unique
    runQuery(query)
  }

  def updateCodeForUser(code:String,goingok_id:UUID): Either[Throwable,Int] = {
    val time = LocalDateTime.now().toString
    val query = sql"""update users
                    set group_code=$code, register_timestamp=$time
                    where goingok_id=$goingok_id
                """.update.run

    runQuery(query)
  }


}
