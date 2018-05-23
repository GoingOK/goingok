/*
 * Copyright 2016-2017 original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import javax.inject.Inject

import org.goingok.server.data.models.GokId
import org.goingok.server.services.AuthorisationService
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, InjectedController}

/**
  * Created by andrew@andrewresearch.net on 22/8/17.
  */

class AuthController @Inject()(authService:AuthorisationService, assets: AssetsFinder) extends InjectedController {

  import scala.concurrent.ExecutionContext.Implicits.global

  def authorise:Action[AnyContent] = Action.async { request =>
    val token = request.body.asText.getOrElse("")
    Logger.debug("TOKEN: "+token)
    val fuid = authService.process(token)
    fuid.map { uid =>
      Logger.debug("User: " + uid)
      val gokId = Json.toJson(GokId(uid))
      Ok(gokId).withSession(
        "uid" -> uid)
    }
  }
}

//def saveStock = Action { request =>
//  val json = request.body.asJson.get
//  val stock = json.as[Stock]
//  println(stock)
//  Ok
//}

// import akka.http.scaladsl.unmarshalling.PredefinedFromEntityUnmarshallers.stringUnmarshaller
//      entity(as[String]) { str =>
//        if (str.isEmpty) complete("An  ID Token is required")
//        else {
//          val authMsg = HandlerMessage.Authorisation(str)
//          onComplete(AuthorisationHandler.process(authMsg)) {gokId =>
//            val id = gokId.get
//            //System.out.println("FINAL ID: ",id)
//            setSession(oneOff, usingHeaders, id) { ctx =>
//              ctx.complete(GokId(id))