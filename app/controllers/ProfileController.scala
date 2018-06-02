package controllers

import auth.DefaultEnv
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.{SecuredErrorHandler, SecuredRequest}
import javax.inject.Inject
import org.goingok.server.data.models.ReflectionEntry
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import views.ProfilePage

import scala.concurrent.{ExecutionContext, Future}

class ProfileController @Inject()(components: ControllerComponents, silhouette: Silhouette[DefaultEnv])
                                 (implicit ec: ExecutionContext, assets: AssetsFinder) extends AbstractController(components) {


  def profile = silhouette.SecuredAction(errorHandler).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>

    val page = ProfilePage.render("GoingOK :: profile",Some(request.identity))
    Future.successful(Ok(page))
  }

  /*
    def profile:Action[AnyContent] = Action.async { request =>
    val optUid = request.session.get("uid")
    if(optUid.isEmpty) {
      Logger.error("The client sent an empty uid")
      Future(Unauthorized("No user id available from session"))
    } else {
      val uid = optUid.get
      Logger.info("Getting profile for: " + uid)
      clientService.getFullProfileForGoingokId(uid).map { profile =>
        if(uid.nonEmpty) { Ok(Json.toJson(profile)) }
        else { Unauthorized("No user id available from session") }
      }
    }

  }

   */

  def reflectionsCsv:Action[AnyContent] = Action.async { request =>
    /*
    val optUid = request.session.get("uid")
    if(optUid.isEmpty) {
      Logger.error("The client sent an empty uid")
      Future(Unauthorized("You are not authorised to access these reflections."))
    } else {
      val uid = optUid.get
      Logger.info("Getting reflections CSV for: " + uid)
      clientService.getReflectionsCsvForGoingokId(uid).map { reflectionsStr =>
        if(uid.nonEmpty) { Ok(reflectionsStr) }
        else { Unauthorized("You are not authorised to access these reflections. ") }
      }
    }
*/
    Future(OK("Not implemented"))
  }

  def saveReflection:Action[AnyContent] = Action.async { request =>

    /*
    val optUid = request.session.get("uid")
    val optReflection = request.body.asJson
    if(optUid.isEmpty) {
      Logger.error("The client sent an empty uid")
      Future(Unauthorized("No user id available from session"))
    } else if(optReflection.isEmpty) {
      Logger.error("The client sent an empty reflection")
      Future(BadRequest("No reflection data"))
    } else {
      val uid:String = optUid.get
      val reflection:ReflectionEntry = optReflection.get.as[ReflectionEntry]
      Logger.info("Saving reflection for: " + uid)
      Logger.debug("Reflection: "+reflection.toString)
      clientService.saveReflection(uid,reflection).map { profile =>
        Ok(Json.toJson(profile))

      }
    } */
    Future(OK("Not implemented"))
  }

  val errorHandler = new SecuredErrorHandler {
    override def onNotAuthenticated(implicit request: RequestHeader) = {
      Future.successful(Redirect(routes.ApplicationController.index()))
    }
    override def onNotAuthorized(implicit request: RequestHeader) = {
      Future.successful(Forbidden("local.not.authorized"))
    }
  }

}
