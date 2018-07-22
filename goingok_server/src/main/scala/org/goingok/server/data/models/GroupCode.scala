package org.goingok.server.data.models

import java.util.UUID

case class GroupCode(group_code:String,created_timestamp:Option[String]=None,owner_goingok_id:Option[UUID]=None)