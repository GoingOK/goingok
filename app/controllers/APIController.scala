package controllers

import org.goingok.server.data.Permissions
import org.goingok.server.data.models.User
import org.goingok.server.services.{APIService, AnalyticsService, ProfileService}
import play.api.libs.json.{JsError, JsNull, JsObject, JsSuccess, JsValue}
import play.api.mvc._

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class APIController @Inject()(components: ControllerComponents, profileService: ProfileService, apiService:APIService)
  (implicit ec: ExecutionContext) extends AbstractController(components) with GoingOkController {

  def logUiActivity: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,logActivity)}

  private val logActivity = (user: User, request: Request[AnyContent]) => {
    val query = request.body.asJson
    logger.debug(query.toString())
    val json = query.getOrElse[JsValue](JsObject(Seq("event" -> JsNull)))
    val eventResult = (json \ "event").validate[String]
    eventResult match {
      case JsSuccess(event, _) =>
        apiService.registerUIActivity(user.goingok_id,event)
        logger.info(s"UI event posted for ${user.goingok_id}: $event")
      case e: JsError =>
        logger.warn("Empty UI event posted to logUiActivity()")
    }
    Ok(eventResult.toString())
    //val event = query.getOrElse(Map()).getOrElse("event",Vector("")).head
//    val response = if(event == JsError) {
//      logger.warn("Empty UI event posted to logUiActivity()")
//      "ERROR: Empty UI event"
//    } else {
//      //apiService.registerUIActivity(user.goingok_id,event)
//      logger.info(s"UI event posted for ${user.goingok_id}: $event")
//      "UI event posted"
//    }
//    Ok(response)
  }

  private def authorise(request: Request[AnyContent], pageMaker: (User, Request[AnyContent]) => Result) = Future {
    val user: Option[User] = for {
      uid <- request.session.get("user")
      u <- profileService.getUser(UUID.fromString(uid))
    } yield u

    user match {
      case Some(usr) => pageMaker(usr, request)
      case None => Unauthorized(UNAUTHORIZED_MESSAGE)
    }

  }
}

