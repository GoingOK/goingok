package controllers

import javax.inject.Inject
import org.goingok.server.Config
import org.goingok.server.data.models.{GoogleUser, User}
import org.goingok.server.services.{GoogleAuthService, UserService}
import play.api.Logger
import play.api.mvc.{Action, AnyContent, _}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class AuthController @Inject()(components: ControllerComponents,authService:GoogleAuthService,userService:UserService)
                              (implicit ex: ExecutionContext) extends AbstractController(components) {

  val logger: Logger = Logger(this.getClass)

  private lazy val baseUrl = Config.string("app.baseurl")
  private lazy val clientid = Config.string("google.client.id")
  private lazy val secret = Config.string("google.client.secret")
  private lazy val redirectUrl = Config.string("google.redirect.url")

  def signin: Action[AnyContent] = Action {
    Redirect(authService.url)
  }

  def signout: Action[AnyContent] = Action {
    Redirect("/").withNewSession
  }

  def googleAuth: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val googleUser = for {
        code <- Try(request.queryString.get("code").map(_.head)).toEither
        gu <- authService.parseAuthCode(code.get)
      } yield gu

      logger.info(s"Get GoogleUser from Google result: ${googleUser.isRight}")

      val user = for {
        gu <- googleUser
        usr <- userService.getUser(gu)
      } yield usr

      logger.info(s"Get User from DB result: $user")

      val newUser = for {
        nu <- handleNewUser(user,googleUser)
      } yield nu

      logger.info(s"handleNewUser result: $newUser")

      newUser match {
        case Right(u) => {
          logger.info(s"Sucessful login for ${u.goingok_id.toString} [${u.pseudonym}]")
          val url = if(registeredUser(u)) { "/profile"} else { "/register" }
          Redirect(url).withSession("user" -> u.goingok_id.toString)
        }
        case Left(e) => {
          logger.error(s"ERROR: ${e.getMessage}")
          if(e.getMessage.contains(UserService.NOT_REGISTERED_ERROR)) {
            Redirect("")
          }

          Redirect("/").withNewSession
        }
        case _ => {
          logger.error("A problem occured during the authentication process.")
          Redirect("/").withNewSession
        }
      }



    }
  }

  private def handleNewUser(user:Either[Throwable,User],googleUser:Either[Throwable,GoogleUser]):Either[Throwable,User] = user match {
    case Right(usr) => Right(usr)
    case Left(e) => {
      if(e.getMessage.contains("ResultSet exhausted;")) {
        logger.warn("This user appears to be logging in for the first time. Create new GoingOK User")
        for {
          gu <- googleUser
          usr <- userService.createUser(gu)
        } yield usr
      } else {
        Left(e)
      }
    }
  }

  private def registeredUser(user:User):Boolean = user.group_code.nonEmpty &&
    !user.group_code.contains("none") &&
    user.register_timestamp.nonEmpty

//  private def checkRegistration(user:User) :Either[Throwable,User] = if(user.group_code.isEmpty || user.group_code.contains("none")) {
//    Left(new Exception(UserService.NOT_REGISTERED_ERROR))
//  } else {
//    Right(user)
//  }

}

