package org.goingok.server.data.models

import java.util.UUID

case class Activity(
                     timestamp:String,
                     goingok_id:UUID,
                     activity_type:String,
                     activity_detail:String)
extends TimestampedData
