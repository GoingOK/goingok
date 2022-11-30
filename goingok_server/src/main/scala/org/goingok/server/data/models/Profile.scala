package org.goingok.server.data.models

import org.goingok.server.data.Message

//case class Profile(
//                    id:String = "",
//                    messages:List[Message]=List(),
//                    reflectionEntries:List[ReflectionEntry]=List(),
//                    research:Research = Research()
//                  )

//object Profile {
//  import play.api.libs.json._
//
//  implicit val ProfileWrites:Writes[Profile] = new Writes[Profile] {
//
//    def writes(profile: Profile):JsObject = Json.obj(
//      "id" -> profile.id,
//      "messages" -> messageWrites(profile.messages),
//      "reflectionEntries" -> profile.reflectionEntries,
//      //"research" -> profile.research
//    )
//
//
//  }
//
//  def messageWrites(messages: List[Message]): List[JsObject] = messages.map { m =>
//    Json.obj(
//      "timestamp" -> m.timestamp,
//      "title" -> m.title,
//      "text" -> m.text,
//      "value" -> m.value,
//      "goingok_id" -> m.goingok_id
//    )
//  }
//
//
//}