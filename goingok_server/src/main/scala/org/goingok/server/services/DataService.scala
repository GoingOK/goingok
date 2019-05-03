package org.goingok.server.services

import java.time.LocalDateTime

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import java.util.UUID

import com.typesafe.scalalogging.Logger
import doobie.util.meta.AdvancedMeta
import org.goingok.server.Config
import org.goingok.server.data.DbResults
import org.goingok.server.data.DbResults.Result
import org.goingok.server.data.models._

import scala.collection.immutable.HashMap

class DataService {

  val logger = Logger(this.getClass)

  private lazy val driver = Config.string("db.driver")
  private lazy val url = Config.string("db.url")
  private lazy val user = Config.string("db.user")
  private lazy val password = Config.string("db.password")
  private lazy val db = Transactor.fromDriverManager[IO](driver,url,user,password)

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

  def insertNewUser(user:User):Either[Throwable,UUID] = {
    logger.info(s"Inserting new user: $user")
    val query = sql"""insert into users (goingok_id,pseudonym,research_code,research_consent,supervisor,admin,group_code,register_timestamp)
                      values(${user.goingok_id},${user.pseudonym},
                            ${user.research_code},${user.research_consent},
                            ${user.supervisor},${user.admin},
                            ${user.group_code},${user.register_timestamp}
                            )
                   """.update
                  .withUniqueGeneratedKeys[UUID]("goingok_id")

    runQuery(query)
  }

  def getUserForId(goingok_id:UUID):Either[Throwable,User] = {
    logger.info(s"Getting user for goingok_id: $goingok_id")
    val query = sql"""select * from users
                 where goingok_id = $goingok_id::uuid
              """.query[User].unique

    runQuery(query)
  }

  def getReflectionsForUser(goingokId:UUID): Either[Throwable,Vector[ReflectionEntry]] = {
    val query = sql"""select timestamp, point, text
                  from reflections
                  where goingok_id=$goingokId
                  order by timestamp desc""".query[ReflectionEntry]

    runQuery(query.to[Vector])
  }

  def getPermission(goingok_id:UUID,group_code:String): Either[Throwable,DbResults.Result] = {
    val query = sql"""select permission
                      from group_permissions
                      where goingok_id=$goingok_id::uuid
                      and group_code=$group_code""".query[String].unique
    runQuery(query).map(r => DbResults.Permission(r))
  }

  def getReflectionsForGroup(group_code:String): Either[Throwable,DbResults.Result] = {
    val query = sql"""select timestamp, point, text
                  from reflections r, users u
                  where r.goingok_id = u.goingok_id
                  and u.group_code=$group_code""".query[ReflectionEntry]

    runQuery(query.to[Seq]).map(r => DbResults.GroupedReflections(r))
  }

  def getReflectionsForGroupWithRange(group_code:String,start:String,end:String): Either[Throwable,DbResults.Result] = {
    val query = sql"""select timestamp, point, text
                  from reflections r, users u
                  where r.goingok_id = u.goingok_id
                  and u.group_code=$group_code
                  and timestamp >=$start
                  and timestamp <=$end""".query[ReflectionEntry]
    runQuery(query.to[Seq]).map(r => DbResults.GroupedReflections(r))
  }

  def getGroupCode(code:String): Either[Throwable,GroupCode] = {
    val query = sql"""select * from group_codes
                      where group_code=$code""".query[GroupCode].unique
    runQuery(query)
  }

  def getAllGroupCodes: Either[Throwable,DbResults.Result] = {
    val query = sql"""select * from group_codes""".query[GroupCode]
    runQuery(query.to[Seq]).map(r => DbResults.GroupCodes(r))
  }


  def updateCodeForUser(code:String,goingok_id:UUID): Either[Throwable,Int] = {
    val time = LocalDateTime.now().toString
    val query = sql"""update users
                    set group_code=$code, register_timestamp=$time
                    where goingok_id=$goingok_id
                """.update.run

    runQuery(query)
  }

  def insertReflection(reflection:ReflectionData,goingok_id:UUID): Either[Throwable,Int] = {
    val time = LocalDateTime.now().toString
    val query = sql"""insert into reflections (timestamp, point, text, goingok_id)
                    values ($time,${reflection.point},${reflection.text},$goingok_id)
                """.update.run

    runQuery(query)
  }

  def insertGroup(groupCode:String,goingok_id:UUID): Either[Throwable,Int] = {
    val time = LocalDateTime.now().toString
    val query = sql"""insert into group_codes (group_code, created_timestamp, owner_goingok_id)
                    values ($groupCode,$time,$goingok_id)
                """.update.run
    runQuery(query)
  }

  def getPseudonymCounts: Either[Throwable,DbResults.Result] = {
    val query = sql"""select allocated,count(pseudonym) from pseudonyms
                      group by allocated
                      """.query[(Option[String],Int)]
    runQuery(query.to[Seq]).map(r => DbResults.GroupedPseudonymCounts(r))
  }

  def getAllPseudonyms: Either[Throwable,Vector[String]] = {
    val query = sql"""select pseudonym from pseudonyms""".query[String]
    runQuery(query.to[Vector])
  }

  def insertPseudonyms(pseudocodes:List[String]): Either[Throwable, Int] = {
    val query = "insert into pseudonyms (pseudonym) values (?)"
    val batch = Update[String](query).updateMany(pseudocodes)
    runQuery(batch)
  }

  def getNextPseudonym: Either[Throwable,String] = {
    val query = sql"""select pseudonym from pseudonyms where allocated is not true limit 1""".query[String].unique
    runQuery(query)
  }

  def updatePseudonym(pseudonym:String): Either[Throwable,Int] = {
    val query = sql"""update pseudonyms set allocated = true where pseudonym = $pseudonym""".update.run
    runQuery(query)
  }

  def countUsers:Either[Throwable,DbResults.Result] = {
    val query = sql"""SELECT group_code, COUNT(*)
                      FROM users
                      GROUP BY group_code
                      ORDER BY group_code
      """.query[(String,Int)]
    runQuery(query.to[Seq]).map(r => DbResults.GroupedUserCounts(r))
  }

  def countReflections(goingok_id:UUID):Either[Throwable,DbResults.Result] = {
    val query = sql"""select u.group_code, count(*)
                     from users u,reflections r
                     where u.goingok_id = r.goingok_id
                     and u.group_code in (
                      select p.group_code
                      from group_permissions p
                      where p.goingok_id=$goingok_id
                     )
                     group by u.group_code
                     order by u.group_code
      """.query[(String, Int)]
    runQuery(query.to[Seq]).map(r => DbResults.GroupedReflectionCounts(r))
  }
}
