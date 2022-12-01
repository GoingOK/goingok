package controllers

import java.util.UUID
import javax.inject.Inject
import org.goingok.server.data.AuthorProfile
import org.goingok.server.data.models.{Author, AuthorAnalytics, ReflectionData, UiMessage, User, UserPseudonym}
import org.goingok.server.services.{AnalyticsService, ProfileService}
import play.api.mvc._
import views.ProfilePage

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
  * Handles GoingOK user profile,
  * Saves new reflections,
  * and gets reflections data
  * @param components
  * @param profileService
  * @param ec
  * @param assets
  */
class ProfileController @Inject()(components: ControllerComponents,profileService:ProfileService, analyticsService:AnalyticsService)
                                 (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {


  /** Processes user reflection */
  def profile: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("user").map { uid =>
      Future {
        val formValues = request.body.asFormUrlEncoded
        logger.debug(s"formValues: ${formValues.toString}")
        val goingok_id = UUID.fromString(uid)
        val user = profileService.getUser(goingok_id)

        val message: Option[UiMessage] = if(formValues.nonEmpty && user.nonEmpty) {
          saveReflection(formValues.get,user.get) match {
            case Right(rows) => if(rows==1) {
              Some(UiMessage("Reflection saved","success"))
            } else {
              val errMsg = s"The reflection didn't save - please try again."
              logger.error(errMsg)
              Some(UiMessage(errMsg,"danger"))
            }
            case Left(error) => {
              val errMsg = s"Unable to save reflection: ${error.getMessage}"
              logger.error(errMsg)
              Some(UiMessage(errMsg,"danger"))
            }
          }
        } else {
          None
        }

        // Get analytics for author + associates
        val analytics:Either[Throwable,Map[String,AuthorAnalytics]] = {
          for {
            ais <- profileService.getAssociatedIds(goingok_id)
            ans <- profileService.getAuthorAnalytics(Vector(UserPseudonym(goingok_id, user.get.pseudonym.get)) ++ ais)
          } yield ans.map(r => (r._1.pseudonym,r._2))
        }

        val analyticsFinal: Option[Map[String,AuthorAnalytics]] = analytics match {
          case Right(an) =>
              Some(an)
          case Left(error) =>
            logger.error(s"Error in creating analytics for profile page: ${error.getMessage}")
            None
        }

        if(user.getOrElse(User(UUID.randomUUID())).group_code!="none") {

          val author = user.map(new Author(_)) // New profile page uses Author instead of User
          val flags = Map("tester" -> isTester(user),"mcm-experiment" -> isExp(request))

          val authorAnalyticsFinal = for {
            a <- author
            af <- analyticsFinal
          } yield af.get(a.pseudonym.getOrElse(""))
          Ok(new ProfilePage(AuthorProfile(author,authorAnalyticsFinal.flatten),Vector(),flags).buildPage(message = message))
        } else {
          Redirect("/register") // User is not registered
        }
      }
    }.getOrElse {
      Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
    }
  }

  /** Save reflection to user profile */
  private def saveReflection(formValues:Map[String,Seq[String]],user: User): Either[Throwable, Int] = {
    for {
      rd <- getReflectionData(formValues)
      res <- profileService.saveReflection(rd,user.goingok_id)
    } yield res
  }

  /** Gets reflection data */
  private def getReflectionData(formValues:Map[String,Seq[String]]):Either[Throwable,ReflectionData] = Try {
    val point = formValues("reflection-point").head.toDouble
    val text = formValues("reflection-text").head
    ReflectionData(point,text)
  }.toEither

  private def isTester(user: Option[User]): Boolean = {
    user match {
      case Some(usr) => usr.supervisor & (analyticsService.getTesters().contains(usr.pseudonym.get))
      case None => false
    }
  }

  private def isExp(request: Request[AnyContent]): Boolean = {
    request.getQueryString("exp").contains("true");
  }

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
