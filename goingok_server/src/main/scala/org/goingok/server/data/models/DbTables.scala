package org.goingok.server.data.models

/**
  * Created by andrew@andrewresearch.net on 11/4/17.
  */

import java.util.UUID

import play.api.libs.json.JsValue





case class UserAuth(
                   goingok_id:UUID = UUID.randomUUID(),
                   google_id:String,
                   google_email:String
                   )

case class User (
                  goingok_id:UUID,
                  pseudonym:String,
                  research_code:String,
                  research_consent:Boolean = false,
                  supervisor:Boolean = false,
                  admin:Boolean = false
                )

case class Message(
                    timestamp:String,
                    title:String,
                    text:String,
                    value: JsValue,
                    goingok_id:UUID
                  )

case class Reflection(
                    timestamp:String,
                    point:Double,
                    text:String,
                    goingok_id:UUID
                  )

