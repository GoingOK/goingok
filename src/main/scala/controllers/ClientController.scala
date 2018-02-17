package controllers

import javax.inject.Inject

import org.goingok.data.models.ReflectionEntry
import org.goingok.services.ClientService
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, InjectedController}

import scala.concurrent.{ExecutionContext, Future}

class ClientController @Inject()(clientService:ClientService)(implicit ec: ExecutionContext) extends InjectedController {

  def profile:Action[AnyContent] = Action.async { request =>
    val uid = request.session.get("uid").getOrElse("")
    Logger.info("Getting profile for: " + uid)
    clientService.getFullProfileForGoingokId(uid).map { profile =>
      if(uid.nonEmpty) { Ok(Json.toJson(profile)) }
      else { Unauthorized("No user id available from session") }
    }
  }

  def saveReflection:Action[AnyContent] = Action.async { request =>
    val optUid = request.session.get("uid")
    val optReflection = request.body.asJson
    if(optUid.isEmpty) {
      Future(Unauthorized("No user id available from session"))
    } else if(optReflection.isEmpty) {
      Future(BadRequest("No reflection data"))
    } else {
      val uid:String = optUid.get
      val reflection:ReflectionEntry = optReflection.get.as[ReflectionEntry]
      Logger.info("Saving reflection for: " + uid)
      Logger.debug("Reflection: "+reflection.toString)
      clientService.saveReflection(uid,reflection).map { profile =>
        Ok(Json.toJson(profile))

      }
    }
  }
  //  ProfileService.saveReflection(msg.id,msg.reflection)

  // def saveResearch
  //  ProfileService.saveResearch(msg.id,msg.research)
}
