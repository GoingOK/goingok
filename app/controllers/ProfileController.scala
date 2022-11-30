package controllers

import java.util.UUID
import javax.inject.Inject
import org.goingok.server.data.{AuthorProfile, Profile, UiMessage}
import org.goingok.server.data.models.{Author, AuthorAnalytics, ReflectionData, ReflectionEntry, User, UserPseudonym}
import org.goingok.server.services.{AnalyticsService, ProfileService}
import play.api.Logger
import play.api.mvc._
import views.ProfilePage

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
  * Handles GoingOK user profile,
  * Saves new reflections,
  * and gets reflections data
  * @param components
  * @param profileService
  * @param ec
  * @param assets
  */
class ProfileController @Inject()(components: ControllerComponents,profileService:ProfileService, analyticsService:AnalyticsService)
                                 (implicit ec: ExecutionContext, assets: AssetsFinder)
  extends AbstractController(components) with GoingOkController {




 //   private val dummyUid = UUID.randomUUID()
//    private val dummyData = Vector(
//      ReflectionDB("2018-05-02T06:01:29.077Z",100.0,"This is a dummy reflection. Number 1.",dummyUid),
//      Reflection("2018-06-03T06:01:29.077Z",50.0,"This is a dummy reflection Number 2. This is a dummy reflection Number 2. This is a dummy reflection Number 2.",dummyUid),
//      Reflection("2018-08-04T06:01:29.077Z",0.0,"This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3. This is a dummy reflection Number 3.",dummyUid),
//      Reflection("2018-08-20T06:01:29.077Z",70.0,"This is a dummy reflection. Number 4.",dummyUid),
//    )

  /** Processes user reflection */
  def profile: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.session.get("user").map { uid =>
      Future {
        val formValues = request.body.asFormUrlEncoded
        logger.debug(s"formValues: ${formValues.toString}")
        val goingok_id = UUID.fromString(uid)
        val user = profileService.getUser(goingok_id)

        val message: Option[UiMessage] = if(formValues.nonEmpty && user.nonEmpty) {
          saveReflection(formValues.get,user.get) match {
            case Right(rows) => if(rows==1) {
              Some(UiMessage("Reflection saved","success"))
            } else {
              val errMsg = s"The reflection didn't save - please try again."
              logger.error(errMsg)
              Some(UiMessage(errMsg,"danger"))
            }
            case Left(error) => {
              val errMsg = s"Unable to save reflection: ${error.getMessage}"
              logger.error(errMsg)
              Some(UiMessage(errMsg,"danger"))
            }
          }
        } else {
          None
        }

        //val associated_ids:Vector[UUID] = Vector(goingok_id,UUID.fromString(""))
        //val associated_ids:Either[Throwable,Vector[UserPseudonym]] = profileService.getAssociatedIds(goingok_id)
        val analytics:Either[Throwable,Map[String,AuthorAnalytics]] = {
          for {
            ais <- profileService.getAssociatedIds(goingok_id)
            ans <- profileService.getAuthorAnalytics(Vector(UserPseudonym(goingok_id, user.get.pseudonym.get)) ++ ais)
          } yield ans.map(r => (r._1.pseudonym,r._2))
          //profileService.getAuthorAnalytics(associated_ids)
        } //goingok_id)
        //logger.warn(analytics.toString)
        //val reflections:String = profileService.getReflections(goingok_id).map(_.reverse)
        //val reflections = profileService.getAuthorReflections(goingok_id) // new reflections format for analytics
        //logger.warn(s"GRAPHS: ${analytics.map(_.graphs)}")
        //logger.warn(s"NODES: ${analytics.map(_.nodes)}")
        //logger.warn(s"EDGES: ${analytics.map(_.edges)}")
        //logger.warn(s"CHARTS: ${analytics.map(_.charts)}")
        //logger.warn(s"NODELABELS: ${analytics.map(_.nodeLabels)}")
        //logger.warn(s"EDGELABELS: ${analytics.map(_.edgeLabels)}")
        //logger.warn(s"LABELS: ${analytics.map(_.labels)}")
        //val chartAnalytics = profileService.getAuthorChartsAnalytics(analytics.toOption.getOrElse(Map()))
        //logger.warn(s"ChartData: ${chartAnalytics}")

//        // for compatibility with prior reflections
//        val reflections: Option[Map[String,Vector[ReflectionEntry]]] = analytics match {
//          case Right(an) => {
//            val refmap = an.map{r =>
//              val u = r._1
//              val refs = r._2.refs
//              u -> refs
//            }
//              Some(refmap)
//          }
//          //Some(an.head._2.refs.map(r => ReflectionEntry(r.ref_id, r.timestamp, ReflectionData(r.point, r.text))).reverse)
//          case Left(error) => {
//            logger.error(error.getMessage)
//            None
//          }
//        }

        // transition - handle analytics separately - to be integrated with reflections above in unified profile

        val analyticsFinal: Option[Map[String,AuthorAnalytics]] = analytics match {
          case Right(an) =>
              Some(an)
          case Left(error) =>
            logger.error(s"Error in creating analytics for profile page: ${error.getMessage}")
            None
        }

        if(user.getOrElse(User(UUID.randomUUID())).group_code!="none") {
//          val page = ProfilePage.page("GoingOK :: profile", message, Profile(user, reflections))
//          Ok(ProfilePage.getHtml(page))
          val author = user.map(new Author(_))
          val flags = Map("tester" -> isTester(user),"mcm-experiment" -> isExp(request))
          // Profile(user,reflections), analyticsFinal
          val authorAnalyticsFinal = for {
            a <- author
            af <- analyticsFinal
          } yield(af.get(a.pseudonym.getOrElse("")))
          Ok(new ProfilePage(AuthorProfile(author,authorAnalyticsFinal.flatten),Vector(),flags).buildPage(message = message))
        } else {
          Redirect("/register")
        }
      }
    }.getOrElse {
      Future.successful(Unauthorized(UNAUTHORIZED_MESSAGE))
    }
  }

  /** Save reflection to user profile */
  private def saveReflection(formValues:Map[String,Seq[String]],user: User): Either[Throwable, Int] = {
    for {
      rd <- getReflectionData(formValues)
      res <- profileService.saveReflection(rd,user.goingok_id)
    } yield res
  }

  /** Gets reflection data */
  private def getReflectionData(formValues:Map[String,Seq[String]]):Either[Throwable,ReflectionData] = Try {
    val point = formValues.get("reflection-point").get.head.toDouble
    val text = formValues.get("reflection-text").get.head
    ReflectionData(point,text)
  }.toEither

  private def isTester(user: Option[User]): Boolean = {
    user match {
      case Some(usr) => usr.supervisor & (analyticsService.getTesters().contains(usr.pseudonym.get))
      case None => false
    }
  }

  private def isExp(request: Request[AnyContent]): Boolean = {
    request.getQueryString("exp").contains("true");
  }

//  def reflectionsCsv :Action[AnyContent] = silhouette.SecuredAction(errorHandler).async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
//
////      Logger.info("Getting reflections CSV for: " + uid)
////      clientService.getReflectionsCsvForGoingokId(uid).map { reflectionsStr =>
////        if(uid.nonEmpty) { Ok(reflectionsStr) }
////        else { Unauthorized("You are not authorised to access these reflections. ") }
////      }
//
//    Future.successful(OK())
//  }


}
