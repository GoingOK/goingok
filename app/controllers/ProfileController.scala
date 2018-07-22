package controllers

//import auth.DefaultEnv
//import com.mohiva.play.silhouette.api.Silhouette
//import com.mohiva.play.silhouette.api.actions.{SecuredErrorHandler, SecuredRequest}
import java.util.UUID

import javax.inject.Inject
import org.goingok.server.data.{Profile, Reflection, UiMessage, models}
import org.goingok.server.data.models.{ReflectionData, ReflectionEntry, User}
import org.goingok.server.services.{ProfileService, UserService}
import play.api.Logger
import play.api.mvc._
import views.{ProfilePage, RegisterPage}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

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
              val errMsg = s"Unable to save reflection: ${error.getMessage.toString}"
              logger.error(errMsg)
              Some(UiMessage(errMsg,"danger"))
            }
          }
        } else {
          None
        }

        val reflections = profileService.getReflections(goingok_id)

        if(user.getOrElse(User(UUID.randomUUID())).group_code!="none") {
          val page = ProfilePage.page("GoingOK :: profile", message, Profile(user, reflections))
          Ok(ProfilePage.getHtml(page))
        } else {
          Redirect("/register")
        }
      }
    }.getOrElse {
      Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
    }
  }

  private def saveReflection(formValues:Map[String,Seq[String]],user: User): Either[Throwable, Int] = {
    for {
      rd <- getReflectionData(formValues)
      res <- profileService.saveReflection(rd,user.goingok_id)
    } yield res
  }

  private def getReflectionData(formValues:Map[String,Seq[String]]):Either[Throwable,ReflectionData] = Try {
    val point = formValues.get("reflection-point").get.head.toDouble
    val text = formValues.get("reflection-text").get.head
    ReflectionData(point,text)
  }.toEither


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
