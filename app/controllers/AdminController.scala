package controllers

import java.util.UUID

import javax.inject.Inject
import org.goingok.server.data.{AdminData, UiMessage}
import org.goingok.server.data.models.User
import org.goingok.server.services.{AdminService, ProfileService}
import play.api.mvc.{AnyContent, _}
import views.AdminPage

import scala.concurrent.{ExecutionContext, Future}

/****
 * AdminController - controls interaction with the admin page
 *
 * @param components
 * @param profileService
 * @param adminService
 * @param ec
 * @param assets
 */
class AdminController @Inject()(components: ControllerComponents,profileService:ProfileService,adminService:AdminService)
                               (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {

  /** Authorises user and calls 'pageMaker' to create an HTML page of reflection analytics */
  def admin: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,makePage)}

  /** Authorises user and calls 'pageMaker' to add a new group */
  def addGroup: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,addNewGroup)}

  def addGroupAdmin: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,addNewGroupAdmin)}

  /** Authorises user and calls 'pageMaker' to add more pseudonyms */
  def addPseudonyms: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,addMorePseudonyms)}

  def addDashboardTester: Action[AnyContent] = Action.async { implicit  request: Request[AnyContent] => authorise(request,addNewTester)}

  //def reflectionsCsv: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] => authorise(request,makeCSV)}

  /**
    * Validates user info and authorizes if the user is an admin
    * @param request the submitted HTTP request
    * @param pageMaker page maker based on user info
    */
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

  private val addNewGroupAdmin = (user:User, request:Request[AnyContent]) => {
    val query = request.queryString
    val formValues = request.body.asFormUrlEncoded
    val result = if (formValues.nonEmpty) {
      System.out.println("formValues:",formValues)
      val pseudonym:String = formValues.get.getOrElse("admin",Vector("")).head
      val group:String = formValues.get.getOrElse("group",Vector("")).head
      adminService.addGroupAdmin(pseudonym,group)
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

  /**
    * Renders an admin page with UI message
    * @param message UI message to display
    * @param user User info
    * @return HTML admin page
    */
  private def makePageWithMessage(message:Option[UiMessage],user:User): Result = {
    val groupInfo = adminService.groupInfo
    val groupAdminInfo = adminService.groupAdminInfo
    val userInfo = adminService.userInfo
    val testers = adminService.testers
    val supervisors = adminService.supervisors
    Ok(new AdminPage(Some(user),AdminData(groupInfo,groupAdminInfo,userInfo,testers,supervisors)).buildPage(message=message))
  }

  private val addMorePseudonyms = (user:User, request:Request[AnyContent]) => {
    adminService.createPseudonyms(1000) match {
      case Right(num) => Ok(s"Created $num pseudonyms")
      case Left(error) => Ok(error.getMessage)
    }
  }

  private val addNewTester = (user:User, request:Request[AnyContent]) => {
    //val query = request.queryString
    val formValues = request.body.asFormUrlEncoded
    val result = if (formValues.nonEmpty) {
      logger.warn(s"formvalues: $formValues")
      val pseudonym: String = formValues.get.getOrElse("tester", Vector("")).head
      adminService.addTester(pseudonym,user.goingok_id)
    } else {
      Left(s"There was a problem adding the tester ${request.body.asText}")
    }
    val message = result match {
      case Right(v) => if (v==1) {
        Some(UiMessage(s"New tester created successfully","info"))
      } else {
        Some(UiMessage(s"There was a problem adding the tester [Pseudonym: $v]", "warn"))
      }
      case Left(e) => Some(UiMessage(s"ERROR: $e", "danger"))
    }
    makePageWithMessage(message,user)
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

