package controllers

import java.util.UUID

import javax.inject.Inject
import org.goingok.server.data.models.{ReflectionData, User}
import org.goingok.server.data.{Analytics, Profile, Reflection, UiMessage}
import org.goingok.server.services.{AnalyticsService, ProfileService}
import play.api.mvc._
import views.{AnalyticsPage, ProfilePage}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class AnalyticsController @Inject()(components: ControllerComponents, profileService:ProfileService, analyticsService:AnalyticsService)
                                   (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {

  def analytics: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val user:Option[User] = for {
        uid <- request.session.get("user")
        u <- profileService.getUser(UUID.fromString(uid))
        if u.supervisor
      } yield u

      user match {
        case Some(usr) => makePage(usr)
        case None => Unauthorized(UNAUTHORIZED_MESSAGE)
      }

    }
  }

  private def makePage(user:User) = {
    val userCounts = analyticsService.groupedUserCounts.getOrElse(Seq())
    val reflectionCounts = analyticsService.groupedReflectionCounts.getOrElse(Seq())
    val message = Some(UiMessage(s"This page is a work in progress. For now, there are only basic stats here. More coming soon.","info"))
    val page = AnalyticsPage.page("GoingOK :: analytics", message,Some(user),Analytics(userCounts,reflectionCounts))
    Ok(AnalyticsPage.getHtml(page))
  }
//    request.session.get("user").map { uid =>
//      Future {
//        val goingok_id = UUID.fromString(uid)
//        val user:Option[User] = profileService.getUser(goingok_id)
//        for {
//          u <- profileService.getUser(goingok_id)
//          if u.supervisor
//        } yield
//
//

//        val formValues = request.body.asFormUrlEncoded
//        logger.debug(s"formValues: ${formValues.toString}")

//
//        val message: Option[UiMessage] = if(formValues.nonEmpty && user.nonEmpty) {
//          saveReflection(formValues.get,user.get) match {
//            case Right(rows) => if(rows==1) {
//              Some(UiMessage("Reflection saved","success"))
//            } else {
//              val errMsg = s"The reflection didn't save - please try again."
//              logger.error(errMsg)
//              Some(UiMessage(errMsg,"danger"))
//            }
//            case Left(error) => {
//              val errMsg = s"Unable to save reflection: ${error.getMessage.toString}"
//              logger.error(errMsg)
//              Some(UiMessage(errMsg,"danger"))
//            }
//          }
//
//        } else {
//          None
//        }




//
//        if(user.getOrElse(User(UUID.randomUUID())).group_code!="none") {
//          if()
//
//
//        } else {
//          Redirect("/register")
//        }
//      }
//    }.getOrElse {
//      Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
//    }
//  }

//  private def saveReflection(formValues:Map[String,Seq[String]],user: User): Either[Throwable, Int] = {
//    for {
//      rd <- getReflectionData(formValues)
//      res <- profileService.saveReflection(rd,user.goingok_id)
//    } yield res
//  }
//
//  private def getReflectionData(formValues:Map[String,Seq[String]]):Either[Throwable,ReflectionData] = Try {
//    val point = formValues.get("reflection-point").get.head.toDouble
//    val text = formValues.get("reflection-text").get.head
//    ReflectionData(point,text)
//  }.toEither


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


}
