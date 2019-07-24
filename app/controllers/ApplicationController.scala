package controllers

import akka.event.Logging.Debug
import org.goingok.server.Config
import javax.inject.Inject
import org.goingok.server.data.models.HealthStatus
import play.api.mvc._
import views.{DemoPage, HelpPage, HomePage}

import scala.concurrent.ExecutionContext

class ApplicationController @Inject()(components: ControllerComponents)
                                     (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {


  def index: Action[AnyContent] = Action {
    val db = Config.dbUrl
    if(db.isDefined){ //replace with .isDefined
      val page = HomePage.render("GoingOK :: home")
      Ok(page)
    } else {
      val page = DemoPage.render("GoingOK :: demo")
      Ok(page)
    }
  }

  def health :Action[AnyContent] = Action {
    val healthMessage = HealthStatus(200) // scalastyle:ignore
    Ok(healthMessage.toString)
  }

  def version :Action[AnyContent] = Action {
    Ok("temporarily disabled")
    //Ok(Json.toJson(ServerInfo(BuildInfo.name,BuildInfo.version,BuildInfo.builtAtString)))
  }

//  def help: Action[AnyContent] = Action {
//    val page = HelpPage.render("GoingOK :: help")
//    Ok(page)
//  }

  def demo: Action[AnyContent] = Action {
    val page = DemoPage.render("GoingOK :: demo")
    Ok(page)
  }

}
