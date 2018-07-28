package controllers

import javax.inject.Inject
import org.goingok.server.data.models.HealthStatus
import play.api.mvc._  // scalastyle:ignore
import views.{HelpPage, HomePage}

import scala.concurrent.ExecutionContext

class ApplicationController @Inject()(components: ControllerComponents)
                                     (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {


  def index: Action[AnyContent] = Action {
    val page = HomePage.render("GoingOK :: home")
    Ok(page)
  }

  def health :Action[AnyContent] = Action {
    val healthMessage = HealthStatus(200) // scalastyle:ignore
    Ok(healthMessage.toString)
  }

  def version :Action[AnyContent] = Action {
    Ok("temporarily disabled")
    //Ok(Json.toJson(ServerInfo(BuildInfo.name,BuildInfo.version,BuildInfo.builtAtString)))
  }

  def help: Action[AnyContent] = Action {
    val page = HelpPage.render("GoingOK :: help")
    Ok(page)
  }

}
