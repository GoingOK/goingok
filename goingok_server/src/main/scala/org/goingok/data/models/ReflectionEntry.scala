package org.goingok.data.models

case class ReflectionEntry(timestamp:String,reflection:ReflectionData)

object ReflectionEntry {
  import play.api.libs.functional.syntax._
  import play.api.libs.json._

  implicit val ReflectionEntryWrites = new Writes[ReflectionEntry] {
    def writes(re: ReflectionEntry) = Json.obj(
      "timestamp" -> re.timestamp,
      "reflection" -> re.reflection
    )
  }
  implicit val ReflectionEntryReads: Reads[ReflectionEntry] = (
      (JsPath \ "timestamp").read[String] and
      (JsPath \ "reflection").read[ReflectionData]
    )(ReflectionEntry.apply _)
}
