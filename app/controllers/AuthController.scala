package controllers

import javax.inject.Inject
import org.goingok.server.Config
import org.goingok.server.services.{GoogleAuthService, UserService}
import play.api.Logger
import play.api.mvc.{Action, AnyContent, _}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class AuthController @Inject()(components: ControllerComponents,authService:GoogleAuthService,userService:UserService)
                              (implicit ex: ExecutionContext) extends AbstractController(components) {

  val logger: Logger = Logger(this.getClass)

  private val baseUrl = Config.string("app.baseurl")
  private val clientid = Config.string("google.client.id")
  private val secret = Config.string("google.client.secret")
  private val redirectUrl = Config.string("google.redirect.url")

  def signin: Action[AnyContent] = Action {
    Redirect(authService.url)
  }

  def signout: Action[AnyContent] = Action {
    Ok("Not implemented yet")
  }

  def googleAuth: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val user = for {
        code <- Try(request.queryString.get("code").map(_.head)).toEither
        gu <- authService.parseAuthCode(code.get)
        usr <- userService.getRegisteredUser(gu)
      } yield usr

      val url = user match {
        case Left(e) => {
          logger.error(s"ERROR: ${e.getMessage}")
          e.getMessage match {
            case UserService.NOT_REGISTERED_ERROR => "/register"
            case _ => "/"
          }
        }
        case Right(usr) => {
          logger.info(s"GOOD: ${usr.toString}")
          "/profile"
        }
      }
      Redirect(url)
    }
  }



}

