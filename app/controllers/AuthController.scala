package controllers

import auth.{DefaultEnv, GoogleAuthService}
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.impl.providers._
import javax.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


class AuthController @Inject()(components: ControllerComponents, silhouette: Silhouette[DefaultEnv], authService: GoogleAuthService)
                              (implicit ex: ExecutionContext) extends AbstractController(components) with Logger {


  def authenticateWithGoogle :Action[AnyContent] =  Action.async { implicit request: Request[AnyContent] =>
    (authService.registry.get[SocialProvider]("google") match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            user <- authService.user.save(profile)
            authenticator <- authService.cookieAuth.create(profile.loginInfo)
            value <- authService.cookieAuth.init(authenticator)
            result <- authService.cookieAuth.embed(value, Redirect(routes.ProfileController.profile()))
          } yield {
            silhouette.env.eventBus.publish(LoginEvent(user, request))
            result
          }
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with google"))
    }).recover {
      case e: ProviderException =>
        logger.error("Unexpected provider error", e)
        Redirect(routes.ApplicationController.index()).flashing("error" -> "could.not.authenticate")
    }
  }

  def signOut :Action[AnyContent] = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val result = Redirect(routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }
}
