package org.goingok.server.data.models

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

case class ReflectionAuthorEntry(pseudonym:String, reflectionEntry:ReflectionEntry) {
  def this(ref: ReflectionAuthorDB) = this(ref.pseudonym,ReflectionEntry(ref.ref_id,ref.timestamp,ReflectionData(ref.point,ref.text)))
  def bneZonedDateTime: ZonedDateTime =  {
    LocalDateTime.parse(reflectionEntry.timestamp).atZone(ZoneId.of("GMT")).withZoneSameInstant(ZoneId.of("Australia/Brisbane"))
  }
  def bneTimestamp: String = bneZonedDateTime.toLocalDateTime.toString
  def bneDateTimeString: String = bneZonedDateTime.format(DateTimeFormatter.ofPattern("E d MMM Y hh:mm a")) //Mon 23 Jul 2018 12:41 AM


}

//object ReflectionEntry {
//  import play.api.libs.functional.syntax._
//  import play.api.libs.json._
//
//  implicit val ReflectionEntryWrites = new Writes[ReflectionEntry] {
//    def writes(re: ReflectionEntry) = Json.obj(
//      "timestamp" -> re.timestamp,
//      "reflection" -> re.reflection
//    )
//  }
//  implicit val ReflectionEntryReads: Reads[ReflectionEntry] = (
//      (JsPath \ "timestamp").read[String] and
//      (JsPath \ "reflection").read[ReflectionData]
//    )(ReflectionEntry.apply _)
//}
