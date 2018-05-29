package controllers

import auth.DefaultEnv
import com.mohiva.play.silhouette.api.actions.{SecuredErrorHandler, SecuredRequest, UserAwareRequest}
import com.mohiva.play.silhouette.api.{LogoutEvent, Silhouette}
import javax.inject.Inject
import play.api.mvc._
import views.{HelpPage, HomePage, ProfilePage}

import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject()(components: ControllerComponents, silhouette: Silhouette[DefaultEnv])
                                     (implicit ec: ExecutionContext, assets: AssetsFinder) extends AbstractController(components) {


  val errorHandler = new SecuredErrorHandler {
    override def onNotAuthenticated(implicit request: RequestHeader) = {
      Future.successful(Redirect(routes.ApplicationController.index()))
    }
    override def onNotAuthorized(implicit request: RequestHeader) = {
      Future.successful(Forbidden("local.not.authorized"))
    }
  }

  def index = silhouette.UserAwareAction.async {
    implicit request: UserAwareRequest[DefaultEnv,AnyContent] => {
      val page = HomePage.render("GoingOK :: home",request.identity)
      Future.successful(Ok(page))
    }
  }

  def help = silhouette.UserAwareAction.async {
    implicit request: UserAwareRequest[DefaultEnv,AnyContent] => {
      val page = HelpPage.render("GoingOK :: help",request.identity)
      Future.successful(Ok(page))
    }
  }

  def profile = silhouette.SecuredAction(errorHandler).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>

    val page = ProfilePage.render("GoingOK :: profile",Some(request.identity))
        Future.successful(Ok(page))
      }


  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val result = Redirect(routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }
}
