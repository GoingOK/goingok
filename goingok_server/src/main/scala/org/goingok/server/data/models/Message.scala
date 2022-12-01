package org.goingok.server.data.models

import java.util.UUID

case class Message(
                    timestamp:String,
                    title:String,
                    text:String,
                    value: Any,
                    goingok_id:UUID
                  )
