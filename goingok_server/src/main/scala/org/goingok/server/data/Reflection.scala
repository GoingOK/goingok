package org.goingok.server.data

import java.util.UUID

case class Reflection(
                       timestamp:String,
                       point:Double,
                       text:String,
                       goingok_id:UUID
                     )
