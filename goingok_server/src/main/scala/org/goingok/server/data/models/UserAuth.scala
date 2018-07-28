package org.goingok.server.data.models

import java.time.LocalDateTime
import java.util.UUID

case class UserAuth(
                   goingok_id:UUID,
                   google_id:String,
                   google_email:String,
                   init_timestamp:String
                   ) {
  def this(google_id:String,google_email:String) = this(UUID.randomUUID(),google_id,google_email,LocalDateTime.now().toString)
}