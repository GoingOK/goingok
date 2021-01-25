package org.goingok.server.data.models

import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

trait TimestampedData {
  val timestamp:String
  def bneZonedDateTime: ZonedDateTime =  {
    LocalDateTime.parse(timestamp).atZone(ZoneId.of("GMT")).withZoneSameInstant(ZoneId.of("Australia/Brisbane"))
  }
  def bneTimestamp: String = bneZonedDateTime.toLocalDateTime.toString
  def bneDateTimeString: String = bneZonedDateTime.format(DateTimeFormatter.ofPattern("E d MMM Y hh:mm a")) //Mon 23 Jul 2018 12:41 AM

}
