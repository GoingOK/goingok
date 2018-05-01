package org.goingok.server.data.models

import play.api.libs.json._

/**
  * Created by andrew@andrewresearch.net on 2/8/17.
  */

case class ServerInfo(
                     name:String,
                     version:String,
                     buildTime:String
                     )

object ServerInfo {

  implicit object ServerInfoFormat extends Format[ServerInfo] {
    // convert from JSON string to a HealthStatus object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[ServerInfo] = {
      val name = (json \ "name").as[String]
      val version = (json \ "version").as[String]
      val buildTime = (json \ "buildTime").as[String]
      JsSuccess(ServerInfo(name,version,buildTime))
    }

    // convert from HealthStatus object to JSON (serializing to JSON)
    def writes(s: ServerInfo): JsValue = {
      JsObject(Seq(
        "name"->JsString(s.name),
        "version"->JsString(s.version),
        "buildTime"->JsString(s.buildTime))
      )
    }
  }
}
