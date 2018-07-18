package org.goingok.server.services

import java.util.UUID

//import javax.inject.Inject
//import org.goingok.server.data.db.DatabaseOps
//import org.goingok.server.data.models._
//import org.joda.time.DateTime
//import play.api.Logger

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by andrew@andrewresearch.net on 12/4/17.
  */

class ClientService {

}

//class ClientService @Inject()(val dbOps:DatabaseOps)(implicit ec: ExecutionContext) {
//
//  //import org.goingok.GoingOkContext._
//
//  def getFullProfileForGoingokId(goingok_id:String):Future[Profile] = {
//    Logger.info(s"Getting profile for id: $goingok_id")
//    val id = UUID.fromString(goingok_id)
//
//    for {
//      ms <- getMessagesForGoingokId(id)
//      rs <- getReflectionsForGoingokId(id)
//      r <- getResearchForGoingokId(id)
//    } yield Profile(goingok_id,ms,rs,r)
//
//  }
//
//  def getMessagesForGoingokId(goingok_id:UUID):Future[List[Message]] = {
//    dbOps.selectMessagesForGoingokId(goingok_id).map { messages =>
//      if(messages.nonEmpty) {
//        messages.toList
//      } else {
//        Logger.info(s"New user profile - creating welcome message for $goingok_id")
//        val newMessage = Message(DateTime.now.toDateTimeISO.toString,WelcomeMsg.title,WelcomeMsg.text,WelcomeMsg.value,goingok_id)
//        dbOps.insertMessage(newMessage)
//        val msgs = Seq()
//        (msgs :+ newMessage).toList
//      }
//    }
//  }
//
//  def getReflectionsForGoingokId(goingok_id:UUID):Future[List[ReflectionEntry]] = {
//    dbOps.selectReflectionsForGoingokId(goingok_id).map { refs =>
//      refs.map( r => ReflectionEntry(r.timestamp,ReflectionData(r.point,r.text))).toList
//    }
//  }
//
//  def getReflectionsCsvForGoingokId(goingok_id:String):Future[String] = {
//    val id = UUID.fromString(goingok_id)
//    dbOps.selectReflectionsForGoingokId(id).map { refs =>
//      val refList = refs.map { r => s"""${r.timestamp},${r.point},"${r.text}""""}
//      val header = """"TIMESTAMP","REFL_POINT","REFL_TEXT"\n"""
//      header + refList.mkString("\n")
//    }
//  }
//
//
//  def getResearchForGoingokId(goingok_id:UUID):Future[Research] = {
//    dbOps.selectUserForGoingokId(goingok_id).map{ user =>
//      if(user.nonEmpty) buildResearchFromCode(user.get.research_code,user.get.research_consent)
//      else Research()
//    }
//  }
//
//  private def buildResearchFromCode(code:String,consent:Boolean):Research = {
//    val segs = code.split('-')
//    Research(ResearchEntry("",segs.apply(0)),ResearchEntry("",segs.apply(1)),ResearchEntry("",segs.apply(2)),consent)
//  }
//
//  def saveReflection(goingok_id:String,reflection:ReflectionEntry): Future[String] = {
//    Logger.info(s"Saving new reflection for $goingok_id")
//    dbOps.insertReflection(Reflection(reflection.timestamp,reflection.reflection.point,reflection.reflection.text,UUID.fromString(goingok_id)))
//    Future("Your reflection has been saved")
//  }
//
//  def saveResearch(goingok_id:String,research:Research): Future[String] = {
//    Logger.info(s"Updating research for $goingok_id")
//    val code = research.project.code +"-"+ research.organisation.code +"-"+ research.cohort.code
//      dbOps.updateUserResearch(UUID.fromString(goingok_id),code,research.consent)
//    Future("Your research choice has been saved")
//  }
//
//
////  def dummyProfile():Profile = {
////
////    import org.goingok.data.models.Profile
////
////    import scala.concurrent.Future
////
////    val messages = ListBuffer[Message]()
////    messages +=  Message(DateTime.now.toIsoDateTimeString(),this.welcomeTitle,this.welcomeMessage,JString("..AG.."),UUID.randomUUID())
////    val entries = ListBuffer[ReflectionEntry]()
////    entries += ReflectionEntry(DateTime.now.-(60000).toIsoDateTimeString(),ReflectionData(30.0,"This should be the most recent entry"))
////    entries += ReflectionEntry(DateTime.now.-(120000).toIsoDateTimeString(),ReflectionData(70.0,"The oldest entry"))
////    val research =  Research(ResearchEntry("a name","T2T"),ResearchEntry(),ResearchEntry())
////    Profile("ThisIsMyDummyId",messages.toList,entries.toList,research)
////  }
//
//
//
//}
//
//
