package org.goingok.server.data.models

import java.time.LocalDateTime
import java.util.UUID

case class UserAuth(
                   goingok_id:UUID,
                   service_id:String,
                   service_email:String,
                   init_timestamp:String
                   ) {
  def this(google_id:String,google_email:String) = this(UUID.randomUUID(),google_id,google_email,LocalDateTime.now().toString)
  def this(cognito_id:UUID) = this(UUID.randomUUID(),cognito_id.toString,"no_email_for_cognito",LocalDateTime.now().toString)
}