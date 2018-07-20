package controllers

//import auth.DefaultEnv
//import com.mohiva.play.silhouette.api.Silhouette
//import com.mohiva.play.silhouette.api.actions.{SecuredErrorHandler, SecuredRequest}
import javax.inject.Inject
import org.goingok.server.data.models
import org.goingok.server.data.models.User
import play.api.Logger
import play.api.mvc._
import views.{ProfilePage, RegisterPage}

import scala.concurrent.{ExecutionContext, Future}

class ProfileController @Inject()(components: ControllerComponents) //, silhouette: Silhouette[DefaultEnv])
                                 (implicit ec: ExecutionContext, assets: AssetsFinder) extends AbstractController(components) {


  def profile: Action[AnyContent] = Action.async {
    val page = ProfilePage.render("GoingOK :: profile",None)
    Future.successful(Ok(page))
  }

//  def profile :Action[AnyContent] = silhouette.SecuredAction(errorHandler).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
//
////    clientService.getFullProfileForGoingokId(uid).map { profile =>
////      if(uid.nonEmpty) { Ok(Json.toJson(profile)) }
////      else { Unauthorized("No user id available from session") }
////    }
//
//    val page = ProfilePage.render("GoingOK :: profile",Some(request.identity))
//    Future.successful(Ok(page))
//  }

  def register: Action[AnyContent] = Action.async {
    val page = RegisterPage.render("GoingOK :: register",None)
    Future.successful(Ok(page))
  }

  def registerWithGroup: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    println(s"register form submit: ${request.body.toString}")
    Redirect("/profile")
  }

//  def register :Action[AnyContent] = silhouette.SecuredAction(errorHandler).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
//
//    //    clientService.getFullProfileForGoingokId(uid).map { profile =>
//    //      if(uid.nonEmpty) { Ok(Json.toJson(profile)) }
//    //      else { Unauthorized("No user id available from session") }
//    //    }
//
//    val page = RegisterPage.render("GoingOK :: register",Some(request.identity))
//    Future.successful(Ok(page))
//  }



//  def reflectionsCsv :Action[AnyContent] = silhouette.SecuredAction(errorHandler).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
//
////      Logger.info("Getting reflections CSV for: " + uid)
////      clientService.getReflectionsCsvForGoingokId(uid).map { reflectionsStr =>
////        if(uid.nonEmpty) { Ok(reflectionsStr) }
////        else { Unauthorized("You are not authorised to access these reflections. ") }
////      }
//
//    Future.successful(OK())
//  }

//  def saveReflection:Action[AnyContent] = silhouette.SecuredAction(errorHandler).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
//
//    /*
//    val optUid = request.session.get("uid")
//    val optReflection = request.body.asJson
//    if(optUid.isEmpty) {
//      Logger.error("The client sent an empty uid")
//      Future(Unauthorized("No user id available from session"))
//    } else if(optReflection.isEmpty) {
//      Logger.error("The client sent an empty reflection")
//      Future(BadRequest("No reflection data"))
//    } else {
//      val uid:String = optUid.get
//      val reflection:ReflectionEntry = optReflection.get.as[ReflectionEntry]
//      Logger.info("Saving reflection for: " + uid)
//      Logger.debug("Reflection: "+reflection.toString)
//      clientService.saveReflection(uid,reflection).map { profile =>
//        Ok(Json.toJson(profile))
//
//      }
//    } */
//    Future.successful(OK())
//  }

//  val errorHandler = new SecuredErrorHandler {
//    override def onNotAuthenticated(implicit request: RequestHeader) :Future[Result] = {
//      Future.successful(Redirect(routes.ApplicationController.index()))
//    }
//    override def onNotAuthorized(implicit request: RequestHeader) :Future[Result] = {
//      Future.successful(Forbidden("local.not.authorized"))
//    }
//  }

}
