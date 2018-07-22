package org.goingok.server.data.models

case class ReflectionData(point:Double,text:String)
//
//object ReflectionData {
//  import play.api.libs.functional.syntax._
//  import play.api.libs.json._
//
//  implicit val ReflectionDataWrites = new Writes[ReflectionData] {
//    def writes(rd: ReflectionData) = Json.obj(
//      "point" -> rd.point,
//      "text" -> rd.text
//    )
//  }
//  implicit val ReflectionDataReads: Reads[ReflectionData] = (
//    (JsPath \ "point").read[Double] and
//      (JsPath \ "text").read[String]
//    )(ReflectionData.apply _)
//}