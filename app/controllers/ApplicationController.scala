package controllers

import akka.event.Logging.Debug
import org.goingok.server.Config
import javax.inject.Inject
import org.goingok.server.data.models.HealthStatus
import play.api.mvc._
import views.{DemoPage, GenericPage, HelpPage, HomePage}

import scala.concurrent.ExecutionContext

/**
  * Responsible of application pages rendering
  * @param components [[play.api.mvc.ControllerComponents]]
  * @param ec [[scala.concurrent.ExecutionContext]]
  * @param assets [[controllers.AssetsFinder]]
  */
class ApplicationController @Inject()(components: ControllerComponents)
                                     (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {

  /** Renders home page */
  def index: Action[AnyContent] = Action {
    val db = Config.dbUrl
    if(db.isDefined){ //replace with .isDefined
      Ok(new HomePage().buildPage())
    } else {
      Ok(new DemoPage().buildPage())
    }
  }



  /** Gets health status */
  def health :Action[AnyContent] = Action {
    val healthMessage = HealthStatus(200) // scalastyle:ignore
    Ok(healthMessage.toString)
  }

  /** Renders application version info
    * @note temporarily disabled
    */
  def version :Action[AnyContent] = Action {
    Ok("temporarily disabled")
    //Ok(Json.toJson(ServerInfo(BuildInfo.name,BuildInfo.version,BuildInfo.builtAtString)))
  }


  /** Renders help page */
  def help: Action[AnyContent] = Action {
    Ok(new HelpPage().buildPage())
  }


  /** Renders demo page */
  def demo: Action[AnyContent] = Action {
    Ok(new DemoPage().buildPage())
  }

}
