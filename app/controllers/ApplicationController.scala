package controllers

import auth.DefaultEnv
import com.mohiva.play.silhouette.api.actions.{SecuredErrorHandler, SecuredRequest, UserAwareRequest}
import com.mohiva.play.silhouette.api.{LogoutEvent, Silhouette}
import javax.inject.Inject
import org.goingok.server.data.models.HealthStatus
import play.api.libs.json.Json
import play.api.mvc._
import views.{HelpPage, HomePage, ProfilePage}

import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject()(components: ControllerComponents, silhouette: Silhouette[DefaultEnv])
                                     (implicit ec: ExecutionContext, assets: AssetsFinder) extends AbstractController(components) {


  def index = silhouette.UserAwareAction.async {
    implicit request: UserAwareRequest[DefaultEnv,AnyContent] => {
      val page = HomePage.render("GoingOK :: home",request.identity)
      Future.successful(Ok(page))
    }
  }

  def health:Action[AnyContent] = Action {
    val healthMessage = HealthStatus(200)
    Ok(Json.toJson(healthMessage))
  }

  def version:Action[AnyContent] = Action {
    Ok("temporarily disabled")
    //Ok(Json.toJson(ServerInfo(BuildInfo.name,BuildInfo.version,BuildInfo.builtAtString)))
  }

  def help = silhouette.UserAwareAction.async {
    implicit request: UserAwareRequest[DefaultEnv,AnyContent] => {
      val page = HelpPage.render("GoingOK :: help",request.identity)
      Future.successful(Ok(page))
    }
  }





}
