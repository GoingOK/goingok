package controllers

//import auth.DefaultEnv
//import com.mohiva.play.silhouette.api.Silhouette
//import com.mohiva.play.silhouette.api.actions.{SecuredErrorHandler, SecuredRequest}
import java.util.UUID

import javax.inject.Inject
import org.goingok.server.data.{Profile, Reflection, models}
import org.goingok.server.data.models.{ReflectionEntry, User}
import org.goingok.server.services.{ProfileService, UserService}
import play.api.Logger
import play.api.mvc._
import views.{ProfilePage, RegisterPage}

import scala.concurrent.{ExecutionContext, Future}

class ProfileController @Inject()(components: ControllerComponents,profileService:ProfileService)
                                 (implicit ec: ExecutionContext, assets: AssetsFinder) extends AbstractController(components) {

  val logger: Logger = Logger(this.getClass)

  private val UNAUTHORIZED_MESSAGE = "You need to login to GoingOK to access this page"


    private val dummyUid = UUID.randomUUID()
    private val dummyData = Vector(
      Reflection("2018-05-02T06:01:29.077Z",100.0,"This is a dummy reflection. Number 1.",dummyUid),
      Reflection("2018-06-03T06:01:29.077Z",50.0,"This is a dummy reflection Number 2. This is a dummy reflection Number 2. This is a dummy reflection Number 2.",dummyUid),
      Reflection("2018-08-04T06:01:29.077Z",0.0,"This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3.",dummyUid),
      Reflection("2018-08-20T06:01:29.077Z",70.0,"This is a dummy reflection. Number 4.",dummyUid),
    )

  def profile: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("user").map { uid =>
      Future {
        val goingok_id = UUID.fromString(uid)
        val reflections = profileService.getReflections(goingok_id)
        val user = profileService.getUser(goingok_id)
        if(user.getOrElse(User(UUID.randomUUID())).group_code!="none") {
          val page = ProfilePage.page("GoingOK :: profile", "This is a test message", Profile(user, reflections))
          Ok(ProfilePage.getHtml(page))
        } else {
          Redirect("/register")
        }
      }
    }.getOrElse {
      Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
    }
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
