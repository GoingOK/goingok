package org.goingok.server.services

import com.typesafe.scalalogging.Logger
import org.goingok.server.data.models.Activity

import java.time.LocalDateTime
import java.util.UUID

class APIService {

  val logger = Logger(this.getClass)

  val ds = new DataService

  def registerUIActivity(goingok_id: UUID, detail: String) = {
    val time = LocalDateTime.now().toString
    val dbResult = ds.insertActivity(Activity(time, goingok_id, "ui", detail))
  }

}
