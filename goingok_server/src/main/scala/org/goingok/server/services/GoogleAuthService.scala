package org.goingok.server.services

import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeTokenRequest, GoogleBrowserClientRequestUrl, GoogleIdToken, GoogleTokenResponse}
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import org.goingok.server.Config
import org.goingok.server.data.models.GoogleUser

import scala.collection.JavaConverters._
import scala.util.Try

class GoogleAuthService {

  private lazy val clientId = Config.string("google.client.id")
  private lazy val secret = Config.string("google.client.secret")
  private lazy val redirectUrl = Config.string("google.redirect.url")
  private lazy val scopes = List(
    "https://www.googleapis.com/auth/userinfo.email",
    "https://www.googleapis.com/auth/userinfo.profile").asJava
  private lazy val tokenServer = "https://www.googleapis.com/oauth2/v4/token"


  def url:String = new GoogleBrowserClientRequestUrl(clientId,redirectUrl,scopes)
    .setState("profile")
    .setApprovalPrompt("force")
    .setResponseTypes(List("code").asJava)
    .build()

  def parseAuthCode(code:String):Either[Throwable,GoogleUser] = for {
    tokenResponse <- getTokens(code)
    googleUser <- parseToUser(tokenResponse)
  } yield googleUser

  private def getTokens(code:String):Either[Throwable,GoogleTokenResponse] = Try {
    new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport, JacksonFactory.getDefaultInstance, tokenServer, clientId, secret, code, redirectUrl).execute
  }.toEither

  private def parseToUser(response:GoogleTokenResponse): Either[Throwable, GoogleUser] = Try {
    val idToken: GoogleIdToken = response.parseIdToken()
    val payload = idToken.getPayload
    GoogleUser(
      payload.getSubject,
      payload.get("name").asInstanceOf[String],
      payload.get("given_name").asInstanceOf[String],
      payload.get("family_name").asInstanceOf[String],
      payload.getEmail,
      payload.getEmailVerified,
      payload.get("picture").asInstanceOf[String]
    )
  }.toEither

}
