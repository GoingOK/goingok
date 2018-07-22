package controllers

import java.util.UUID

import javax.inject.Inject
import org.goingok.server.services.UserService
import play.api.Logger
import play.api.mvc._
import views.{ProfilePage, RegisterPage}

import scala.concurrent.{ExecutionContext, Future}

class RegisterController @Inject()(components: ControllerComponents, userService:UserService)
                                  (implicit ec: ExecutionContext, assets: AssetsFinder) extends AbstractController(components) {

  val logger: Logger = Logger(this.getClass)

  private val UNAUTHORIZED_MESSAGE = "You need to login to GoingOK to access this page"

  def register: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("user").map { uid =>
      val goingok_id = UUID.fromString(uid)
      val page = RegisterPage.render("GoingOK :: register")
      Future.successful(Ok(page))
    }.getOrElse {
      Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
    }


  }

  def registerWithGroup: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("user").map { uid =>
      Future {
        val goingok_id = UUID.fromString(uid)
        val formValues = request.body.asFormUrlEncoded
        val code: String = if (formValues.nonEmpty) {
          formValues.get.getOrElse("group-code", Vector("")).head
        } else {
          ""
        }
        userService.isGroupCodeValid(code) match {
          case Right(exists) => {
            userService.addGroupCodeToUser(code,goingok_id) match {
              case Left(error) => logger.error(s"There was a problem updating $code for $goingok_id")
              case Right(i) => logger.info(s"group-code $code updated for $goingok_id [$i]")
            }
            Redirect("/profile")
          }
          case Left(error) => {
            logger.warn(s"An incorrect group-code was entered by $goingok_id")
            val message = s"""$code is not a valid group code. Please check the code and try again."""
            val page = RegisterPage.render("GoingOK :: register", message = message)
            Ok(page)
          }
        }
      }
    }.getOrElse {
      Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
    }
  }


}
