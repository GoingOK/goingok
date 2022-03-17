package controllers

import javax.inject.Inject
import org.goingok.server.Config
import org.goingok.server.data.models.{CognitoUser, User}
import org.goingok.server.services.UserService
import pdi.jwt.JwtBase64
import play.api.Logger
import play.api.libs.ws.{WSAuthScheme, WSClient, WSRequest, WSResponse}
import play.api.mvc.{Action, AnyContent, _}

import java.util.UUID
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try
import scala.concurrent.duration._

/**
  * Handles signin, signout and new users
  * @param components [[play.api.mvc.ControllerComponents]]
  * @param userService [[org.goingok.server.services.UserService]]
  * @param ex [[scala.concurrent.ExecutionContext]]
  */
class CognitoAuthController @Inject()(components: ControllerComponents, userService:UserService, webService:WSClient)
                                     (implicit ex: ExecutionContext) extends AbstractController(components) {

  val logger: Logger = Logger(this.getClass)

  private lazy val clientId = Config.awsCognitoAppClientId.get
  private lazy val authUrl = Config.awsCognitoAuthUrl.get
  private lazy val redirectUrl = Config.awsCognitoRedirectUrl.get

  /** Builds Cognito URL*/
  def url:String = {
    s"${authUrl}/login?response_type=code&client_id=${clientId}&redirect_uri=${redirectUrl}"
  }


  /** Function to handle signin requests */
  def signin: Action[AnyContent] = Action {
    Redirect(url)
  }

  /** Function to handle signout requests */
  def signout: Action[AnyContent] = Action {
    Redirect("/").withNewSession
  }

  /** Function to validate user information with cognito api */
  def cognitoAuth: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Future {
      val cognitoUser = for {
        code <- getCognitoAuth(request)
        cu <- getCognitoUserwithAuthCode(code)
      } yield cu


      cognitoUser match {
        case Right(user) => logger.warn(s"cognitoUser from Cognito result: ${user.toString}")
        case Left(e) => logger.error(s"Unable to get cognitoUser from Cognito result: ${e.toString}")
      }
      val user = for {
        cu <- cognitoUser
        usr <- userService.getUser(cu)
      } yield usr

      logger.info(s"Get User from DB result: $user")

      val newUser = for {
        nu <- handleNewUser(user,cognitoUser)
      } yield nu

      logger.info(s"handleNewUser result: $newUser")

      newUser match {
        case Right(u) => {
          logger.info(s"Sucessful login for ${u.goingok_id.toString} [${u.pseudonym}]")
          val url = if(registeredUser(u)) {
            userService.registerLoginActivity(u.goingok_id,"success - profile page")
            "/profile"
          } else {
            userService.registerLoginActivity(u.goingok_id,"redirected - register page")
            "/register"
          }
          Redirect(url).withSession("user" -> u.goingok_id.toString)
        }
        case Left(e) => {
          logger.error(s"ERROR: ${e.getMessage}")
          if(e.getMessage.contains(UserService.NOT_REGISTERED_ERROR)) {
            Redirect("")
          }

          Redirect("/").withNewSession
        }
        case _ => {
          logger.error("A problem occured during the authentication process.")
          Redirect("/").withNewSession
        }
      }



    }
  }

  private def getCognitoAuth(request: Request[AnyContent]): Either[Throwable, Option[String]] = Try {
    //logger.debug(s"QUERYSTRING: ${request.rawQueryString}")
    request.queryString.get("code").map(_.head)
  }.toEither

  private def getCognitoUserwithAuthCode(code:Option[String]):Either[Throwable,CognitoUser] = {
    //logger.debug(s"Asking for token with code: $code")
    //val clientId = Config.awsCognitoAppClientId.get
    //val baseUrl = "https://goingok-auth.auth.ap-southeast-2.amazoncognito.com"
    val url = authUrl+"/oauth2/token"
    //val redirect = "http://localhost:9000/auth"
    val form = Map(
        "grant_type" -> Seq("authorization_code"),
        "code" -> Seq(code.get),
        "client_id" -> Seq(clientId),
        "redirect_uri" -> Seq(redirectUrl)
    )
    //logger.info(s"form: $form")
    val request: WSRequest = webService.url(url)
    val wsResponse: Future[WSResponse] = request
      .addHttpHeaders("Content-Type" -> "application/x-www-form-urlencoded")
      .withAuth(clientId, Config.awsCognitoAppClientSecret.get, WSAuthScheme.BASIC)
      .post(form)
    logger.debug(wsResponse.toString)
    val fResult = wsResponse.map { response =>
      if (response.status >= 400) {
        // application level error
        val msg = response.json.as[String]
        Left(throw new UnsupportedOperationException(msg))
      } else { // application level good response
        //val header = response.headers.toString
        //logger.debug(s"header: $header")
        val idToken = (response.json \ "id_token").as[String]
        //logger.debug("ID: "+idToken)
//        val access = (response.json \ "access_token").as[String]
//        logger.warn("ACCESS: "+access)
        val cognitoUser = parseIdToken(idToken)
        Right(cognitoUser)
      }
    }
    Await.result(fResult,3.minutes)
  }

  private def parseIdToken(idToken:String):CognitoUser = {
    import play.api.libs.json._

    val tokenParts = idToken.split('.').toList
    val claims = JwtBase64.decodeString(tokenParts(1))

    val jsonClaims: JsValue = Json.parse(claims)
    logger.debug(s"jsonClaims: $jsonClaims")
    val sub = (jsonClaims \ "sub").as[UUID]
    logger.debug("Need to check for google identity for backwards compatibility")
    logger.debug(s"SUB: $sub")
    val identities = (jsonClaims \ "identities" \ 0).toOption
    val cogUser:CognitoUser = identities match {
      case Some(identity) => {
        logger.debug("Found an identity: "+identity.toString())
        val provider = (identity \ "providerName")
        val uid = (identity \ "userId")
        logger.debug(s"PROVIDER: $provider with UID: $uid")
        if(provider.toString.contains("Google")) {
          logger.debug(s"User logging in with Google - add google_id to cognitoUser")
          CognitoUser(sub,Some(uid.get.as[String]))
        } else {
          logger.debug(s"User logging in with ${provider.toString} - no google_id")
          CognitoUser(sub)
        }
      }
      case None => {
        CognitoUser(sub)
      }
    }
    logger.debug(s"COGNITOUSER: $cogUser")
    cogUser
  }

  /**
    * Function to handle new GoingOK user
    * @param user GoingOk user info
    * @param cognitoUser google user info
    */
  private def handleNewUser(user:Either[Throwable,User],cognitoUser:Either[Throwable,CognitoUser]):Either[Throwable,User] = user match {
    case Right(usr) => Right(usr)
    case Left(e) => {
      if(e.getMessage.contains("ResultSet exhausted;")) {
        logger.warn("This user appears to be logging in for the first time. Create new GoingOK User")
        for {
          cu <- cognitoUser
          usr <- userService.createUser(cu)
        } yield usr
      } else {
        Left(e)
      }
    }
  }

  /**
    * Function to check if a user is already registered
    * @param user user info
    */
  private def registeredUser(user:User):Boolean = user.group_code.nonEmpty &&
    !user.group_code.contains("none") &&
    user.register_timestamp.nonEmpty

//  private def checkRegistration(user:User) :Either[Throwable,User] = if(user.group_code.isEmpty || user.group_code.contains("none")) {
//    Left(new Exception(UserService.NOT_REGISTERED_ERROR))
//  } else {
//    Right(user)
//  }

}

