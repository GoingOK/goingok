package org.goingok.server.services

import java.util.Collections
import javax.inject.Inject

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import org.goingok.server.data.models.GoogleUser
import play.api.Logger

import scala.concurrent.ExecutionContext
//import org.goingok.GokId

import scala.concurrent.Future

/**
  * Created by andrew@andrewresearch.net on 3/3/17.
  */
class AuthorisationService @Inject()(val userService:UserService)(implicit ec: ExecutionContext) {

  //import org.goingok.GoingOkContext._

  def process(token:String):Future[String] = {

    val transport:HttpTransport = GoogleNetHttpTransport.newTrustedTransport()
    val jsonFactory = JacksonFactory.getDefaultInstance()

    val verifier = new Builder(transport, jsonFactory)
      .setAudience(Collections.singletonList(Config.string("google.client_id")))
      .build()

    val idToken = verifier.verify(token)

    val payload:Payload = if (idToken != null) idToken.getPayload()
      else {
        Logger.warn("Invalid ID token.")
        new Payload
      }

    val googleUser = GoogleUser(payload.getSubject,payload.get("given_name").toString,payload.get("family_name").toString,payload .getEmail(),payload.getEmailVerified(),payload.get("locale").toString)



    Logger.info(s"User authorised: $googleUser")

   userService.getOrCreateGoingokId(payload.getSubject,payload.getEmail).map(_.toString)
  }


}


//JsonMessage.formatStringResults(s"|$email|$emailVerified|$name|$pictureUrl|$locale|$familyName|$givenName|","The token has been processed")
//JsonMessage.formatResults[Profile](Future(Profile()),s"|$email|$emailVerified|$name|$pictureUrl|$locale|$familyName|$givenName|")


// Print user identifier
//      val userId = payload.getSubject()
//      System.out.println("User ID: " + userId)

// Get profile information from payload
//      val email = payload .getEmail()
//      val emailVerified = payload.getEmailVerified()
//      val name = payload.get("name")
//      val pictureUrl = payload.get("picture")
//      val locale = payload.get("locale")
//      val familyName = payload.get("family_name")
//      val givenName = payload.get("given_name")