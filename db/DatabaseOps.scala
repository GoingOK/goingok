package org.goingok.server.data.db



import java.util.UUID
import javax.inject.Inject

import org.goingok.server.data.models.{Message, Reflection, User, UserAuth}
import org.goingok.server.data.db.Schema.{MessageSchema, ReflectionSchema, UserAuthSchema, UserSchema}
import org.goingok.server.data.db.postgresql.GoingokPostgresProfile.api._
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.{AbstractController, ControllerComponents}
import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}


class DatabaseOps @Inject() (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {


  val userAuths = TableQuery[UserAuthSchema]
  val users = TableQuery[UserSchema]
  val messages = TableQuery[MessageSchema]
  val reflections = TableQuery[ReflectionSchema]
  val allTableQueries = List(userAuths,users,messages,reflections)
  //val tableQueryMap = allTableQueries.map(tq => (tq.baseTableRow.tableName,tq)).toMap


  //------- Whole of database operations

  def version:Future[String] = db.run(sql"SELECT VERSION()".as[String].head)

  def dbTables:Future[Vector[MTable]] = db.run(MTable.getTables)

  def checkAndCreateTables():Unit = {
    val tables = Await.result(dbTables, 2 seconds).toList.map(_.name.name)
    allTableQueries.foreach { tq =>
      if(!tables.contains(tq.baseTableRow.tableName)) {
        Logger.warn("Creating table: "+tq.baseTableRow.tableName)
        db.run(tq.schema.create)
      }
    }
  }

  def tableSizes():Either[(Int, Int, Int, Int), Boolean] = try {
    val result = for {
      uaRows <- db.run(userAuths.length.result)
      uRows <- db.run(users.length.result)
      mRows <- db.run(messages.length.result)
      rRows <- db.run(reflections.length.result)
    } yield (uaRows, uRows, mRows, rRows)
    val rows = Await.result(result, 10 seconds)
    Left(rows)
  } catch {
    case e: Exception => Logger.error("There was an issue getting the row counts: " + e)
      Right(false)
  }



  //------ userAuths

  def insertUserAuth(ua: UserAuth):Future[Int] = db.run(userAuths += ua)

  def selectAllUserAuths:Future[Seq[UserAuth]] = db.run(userAuths.result)

  def selectUserAuthForGoogleId(google_id:String):Future[Option[UserAuth]] = {
    db.run(userAuths.filter(f=> f.google_id === google_id).result).map(r => if(r.isEmpty) None else Some(r.head))
  }

  //------ users

  def insertUser(u: User):Future[Int] = db.run(users += u)

  def selectAllUsers:Future[Seq[User]] = db.run(users.result)

  def selectUserForGoingokId(goingok_id:UUID):Future[Option[User]] = {
    db.run(users.filter(f=> f.goingok_id === goingok_id).result).map(r => if(r.isEmpty) None else Some(r.head))
  }

  def updateUserResearch(goingok_id:UUID,researchCode:String,researchConsent:Boolean) = {
    val update = users.filter(_.goingok_id === goingok_id)
      .map(p => (p.research_code,p.research_consent))
      .update(researchCode,researchConsent)
    db.run(update)
  }

  //------ messages

  def insertMessage(m: Message) = db.run(messages += m)

  def selectAllMessages = db.run(messages.result)

  def selectMessagesForGoingokId(goingok_id:UUID):Future[Seq[Message]] = {
    db.run(messages.filter(f=> f.goingok_id === goingok_id).sortBy(p => (p.timestamp.desc)).result)
  }

  //------ reflections

  def insertReflection(r: Reflection) = db.run(reflections += r)

  def selectAllReflections = db.run(reflections.result)

  def selectReflectionsForGoingokId(goingok_id:UUID):Future[Seq[Reflection]] = {
    db.run(reflections.filter(f=> f.goingok_id === goingok_id).sortBy(p => (p.timestamp.desc)).result)
  }

}

//
//import java.util.UUID
//
//import org.goingok.data.models._
//import org.slf4j.LoggerFactory
//import slick.jdbc.meta.MTable
//
//import scala.concurrent.duration._
//import scala.concurrent.{Await, Future}
//
///**
//  * Created by andrew@andrewresearch.net on 2/09/2016.
//  */
//object DatabaseOps {
//
//  def logger = LoggerFactory.getLogger(this.getClass)
//
//  //import org.goingok.data.persistence.db.postgresql.GoingOkPostgresDriver.api._
//  import scala.concurrent.ExecutionContext.Implicits.global
//  //import GoingOkPostgresDriver.jsonMethods._
//
//  //val db = Database.forConfig("db")
//
//  //--------- Schema Queries for Database Tables
//
//  val userAuths = TableQuery[UserAuthSchema]
//  val users = TableQuery[UserSchema]
//  val messages = TableQuery[MessageSchema]
//  val reflections = TableQuery[ReflectionSchema]
//  val allTableQueries = List(userAuths,users,messages,reflections)
//  //val tableQueryMap = allTableQueries.map(tq => (tq.baseTableRow.tableName,tq)).toMap
//
//
//  //------- Whole of database operations
//
//  def version:Future[String] = db.run(sql"SELECT VERSION()".as[String].head)
//
//  def dbTables:Future[Vector[MTable]] = db.run(MTable.getTables)
//
//  def checkAndCreateTables():Unit = {
//    val tables = Await.result(dbTables, 2 seconds).toList.map(_.name.name)
//    allTableQueries.foreach { tq =>
//      if(!tables.contains(tq.baseTableRow.tableName)) {
//        logger.warn("Creating table: "+tq.baseTableRow.tableName)
//        db.run(tq.schema.create)
//      }
//    }
//  }
//
//  def tableSizes():Either[(Int, Int, Int, Int), Boolean] = try {
//      val result = for {
//        uaRows <- db.run(userAuths.length.result)
//        uRows <- db.run(users.length.result)
//        mRows <- db.run(messages.length.result)
//        rRows <- db.run(reflections.length.result)
//      } yield (uaRows, uRows, mRows, rRows)
//      val rows = Await.result(result, 10 seconds)
//      Left(rows)
//    } catch {
//      case e: Exception => logger.error("There was an issue getting the row counts: " + e)
//        Right(false)
//    }
//
//
//
//  //------ userAuths
//
//  def insertUserAuth(ua: UserAuth):Future[Int] = db.run(userAuths += ua)
//
//  def selectAllUserAuths:Future[Seq[UserAuth]] = db.run(userAuths.result)
//
//  def selectUserAuthForGoogleId(google_id:String):Future[Option[UserAuth]] = {
//    db.run(userAuths.filter(f=> f.google_id === google_id).result).map(r => if(r.isEmpty) None else Some(r.head))
//  }
//
//  //------ users
//
//  def insertUser(u: User):Future[Int] = db.run(users += u)
//
//  def selectAllUsers:Future[Seq[User]] = db.run(users.result)
//
//  def selectUserForGoingokId(goingok_id:UUID):Future[Option[User]] = {
//    db.run(users.filter(f=> f.goingok_id === goingok_id).result).map(r => if(r.isEmpty) None else Some(r.head))
//  }
//
//  def updateUserResearch(goingok_id:UUID,researchCode:String,researchConsent:Boolean) = {
//    val update = users.filter(_.goingok_id === goingok_id)
//      .map(p => (p.research_code,p.research_consent))
//      .update(researchCode,researchConsent)
//    db.run(update)
//  }
//
//  //------ messages
//
//  def insertMessage(m: Message) = db.run(messages += m)
//
//  def selectAllMessages = db.run(messages.result)
//
//  def selectMessagesForGoingokId(goingok_id:UUID):Future[Seq[Message]] = {
//    db.run(messages.filter(f=> f.goingok_id === goingok_id).sortBy(p => (p.timestamp.desc)).result)
//  }
//
//  //------ reflections
//
//  def insertReflection(r: Reflection) = db.run(reflections += r)
//
//  def selectAllReflections = db.run(reflections.result)
//
//  def selectReflectionsForGoingokId(goingok_id:UUID):Future[Seq[Reflection]] = {
//    db.run(reflections.filter(f=> f.goingok_id === goingok_id).sortBy(p => (p.timestamp.desc)).result)
//  }
//
//}
//
////
////  def readIdAllocationWithTag(tag:String) = {
////    val tagged = idAllocationQuery.filter( f => List(tag).bind <@: f.tags).sortBy(_.anon_id).to[List]
////    db.run(tagged.result)
////  }
////
////  //A query that returns idAllocations that don't have the string in tags
////  def idAllocationWithoutTag(tag:String) = idAllocationQuery.filter( f => !(List(tag).bind <@: f.tags) || f.tags==null || f.tags.isEmpty).sortBy(_.anon_id).to[List]
////
////  def readMetadataWithoutIdTag(tag:String) = {
////    val taggedMeta = for { (m,i) <- metadataQuery join idAllocationWithoutTag(tag) on (_.anon_id === _.anon_id) } yield m
////    db.run(taggedMeta.result)
////  }
////
////  def readFeedbackWithoutIdTag(tag:String) = {
////    val taggedFb = for { (f,i) <- feedbackQuery join idAllocationWithoutTag(tag) on (_.anon_id === _.anon_id) } yield f
////    db.run(taggedFb.result)
////  }
////
//
////  def updateMetadata(metadataId:UUID,derivativeId:String,description:String):Future[Int] = {
////    def updater:DBIO[Int] = sqlu"UPDATE tap_metadata SET derivatives = derivatives || hstore($derivativeId,$description) WHERE file_uid = $metadataId;"
////    db.run(updater)
////  }
////
////  def updateMetadata(metadataId:UUID,derivMap:Map[String,String]):Future[Int] = {
////    val meta = Await.result(db.run(metadataQuery.filter( f => f.file_uid === metadataId).result.head), 2 seconds)
////    val newMeta = TapMetadata(meta.user_id,meta.anon_id,meta.content_file_id,meta.metadata,derivMap ++ meta.derivatives,meta.file_uid,meta.file_timestamp)
////    db.run(metadataQuery.insertOrUpdate(newMeta))
////  }
////
////  def updateIdAllocation(hashId:String,tags:List[String]):Future[Int] = {
////    var rowCount = Future(0)
////    logger.debug("Updating idAllocation for "+hashId)
////    db.run(idAllocationQuery.filter(f => f.hash_id === hashId).result).onComplete {
////      case Success(result1) => {
////        logger.debug("Got a result")
////        if(result1.headOption.isEmpty) logger.debug("No data for this result")
////        else {
////          val idAl = result1.head
////          logger.info("Looked for "+hashId+" and found "+idAl.toString)
////          val newIdAl = TapIdAllocation(idAl.hash_id,idAl.anon_id,Some(tags))
////          logger.debug("performing the update")
////          rowCount = db.run(idAllocationQuery.insertOrUpdate(newIdAl))
////        }
////      }
////      case Failure(e) => logger.error("Unable to update idAllocation "+e)
////    }
////    rowCount
////  }
