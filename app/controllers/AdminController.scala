package controllers

import java.util.UUID

import javax.inject.Inject
import org.goingok.server.data.{AdminData, UiMessage}
import org.goingok.server.data.models.User
import org.goingok.server.services.{AdminService, ProfileService}
import play.api.mvc.{AnyContent, _}
import views.AdminPage

import scala.concurrent.{ExecutionContext, Future}

class AdminController @Inject()(components: ControllerComponents,profileService:ProfileService,adminService:AdminService)
                               (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {

  def admin: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,makePage)}

  def addGroup: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,addNewGroup)}

  def addPseudonyms: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,addMorePseudonyms)}

  //def reflectionsCsv: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,makeCSV)}

  private def authorise(request:Request[AnyContent],pageMaker:(User,Request[AnyContent])=>Result) = Future {
    val user: Option[User] = for {
      uid <- request.session.get("user")
      u <- profileService.getUser(UUID.fromString(uid))
      if u.admin
    } yield u

    user match {
      case Some(usr) => pageMaker(usr,request)
      case None => Unauthorized(UNAUTHORIZED_MESSAGE)
    }

  }

  private val makePage = (user: User,request:Request[AnyContent]) => {
    val message = Some(UiMessage(s"This page is a work in progress.", "info"))
    makePageWithMessage(message,user)
  }

  private val addNewGroup = (user:User, request:Request[AnyContent]) => {
    val query = request.queryString
    val formValues = request.body.asFormUrlEncoded
    val result = if (formValues.nonEmpty) {
      val code: String = formValues.get.getOrElse("group-code", Vector("")).head
      adminService.addGroup(code,user.goingok_id)
    } else {
      Left("There was a problem adding the group")
    }
    val message = result match {
      case Right(v) => if (v==1) {
        Some(UiMessage(s"Group created successfully","info"))
      } else {
        Some(UiMessage(s"The group was not created [Code: $v]", "warn"))
      }
      case Left(e) => Some(UiMessage(s"ERROR: $e", "danger"))
    }
    makePageWithMessage(message,user)
  }

  private def makePageWithMessage(message:Option[UiMessage],user:User): Result = {
    val groupInfo = adminService.groupInfo
    val userInfo = adminService.userInfo
    val page = AdminPage.page("GoingOK :: admin", message, Some(user),AdminData(groupInfo,userInfo))
    Ok(AdminPage.getHtml(page))
  }

  private val addMorePseudonyms = (user:User, request:Request[AnyContent]) => {
    adminService.createPseudonyms(2000) match {
      case Right(num) => Ok(s"Created $num pseudonyms")
      case Left(error) => Ok(error.getMessage)
    }
  }

  //  private val makeCSV = (user:User, request:Request[AnyContent]) => {
  //    val query = request.queryString
  //
  //    val group = query.getOrElse("group",Vector("")).head
  //    val range = for(r <- query.get("range").map(_.head)) yield r
  //    val response =if(analyticsService.hasPermission(user.goingok_id,group,Permissions.Sensitive)) {
  //      analyticsService.reflectionsForGroupCSV(group,range).getOrElse("no data")
  //    } else {
  //      "Not Permitted"
  //    }
  //    Ok(response)
  //  }




}

