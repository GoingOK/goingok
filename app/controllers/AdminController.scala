package controllers

import java.util.UUID

import javax.inject.Inject
import org.goingok.server.services.AdminService
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AdminController @Inject()(components: ControllerComponents,adminService:AdminService)
                               (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {


  def admin: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("user").map { uid =>
      Future {
        logger.info(s"Admin uid: $uid")
        Ok("Admin")
      }
    }.getOrElse {
      Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
    }
  }


  def pseudonymAdder: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("user").map(id => UUID.fromString(id)) match {
      case Some(uuid) => {
        adminService.getAdminUser(uuid) match {
          case Right(usr) => {
            adminService.createPseudonyms(1000) match {
              case Right(num) => Future.successful(Ok(s"Created $num pseudonyms"))
              case Left(error) => Future.successful(Ok(error.getMessage))
            }
          }
          case Left(error) => Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
        }
      }
      case None => Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
    }
  }

}

