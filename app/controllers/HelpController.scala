package controllers

import java.util.UUID

import javax.inject.Inject
import org.goingok.server.data.models.User
import org.goingok.server.services.ProfileService
import play.api.mvc._
import views.HelpPage

import scala.concurrent.{ExecutionContext, Future}

class HelpController @Inject()(components: ControllerComponents, profileService:ProfileService)
                              (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {

  def help: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,makePage)}

  private def authorise(request:Request[AnyContent],pageMaker:(Option[User],Request[AnyContent])=>Result) = Future {
    val user: Option[User] = for {
      uid <- request.session.get("user")
      u <- profileService.getUser(UUID.fromString(uid))
      if u.supervisor
    } yield u

    pageMaker(user,request)
  }

  private val makePage = (user: Option[User],request:Request[AnyContent]) => {
    //val message = Some(UiMessage(s"This page is a work in progress. For now, there are only basic stats here. More coming soon.", "info"))
//    val page = HelpPage.page("GoingOK :: help", None, user)
//    Ok(HelpPage.getHtml(page))
    Ok(new HelpPage(user).buildPage())
  }


}
