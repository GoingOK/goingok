package org.goingok.server.services

import java.util.UUID

import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._
import org.goingok.server.Config
import org.goingok.server.data.models.ReflectionEntry

class DataService {

  import doobie.postgres._
  import doobie.postgres.implicits._

  val driver = Config.string("db.driver")
  val url = Config.string("db.url")
  val user = Config.string("db.user")
  val password = Config.string("db.password")

  // A transactor that gets connections from java.sql.DriverManager
  val xa = Transactor.fromDriverManager[IO](driver,url,user,password)


  def getReflectionsForUser(uid:UUID): Seq[ReflectionEntry] = {
   sql"select timestamp, point, text from reflections where goingok_id=$uid".query[ReflectionEntry]
      .to[List]
      .transact(xa)
      .unsafeRunSync
  }

}
