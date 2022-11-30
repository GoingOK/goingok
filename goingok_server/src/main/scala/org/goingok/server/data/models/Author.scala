package org.goingok.server.data.models

import java.util.UUID

case class Author(
                 goingok_id:UUID,
                 pseudonym:Option[String]=None,
                 //research_code:Option[String]=None,
                 research_consent:Boolean = false,
                 supervisor:Boolean = false,
                 admin:Boolean = false,
                 group_code:String = "none",
                 //register_timestamp:Option[String]=None
                 ) {
  // This allows us to build an Author from a DB User
  def this(user: User) = this(user.goingok_id,user.pseudonym,user.research_consent,user.supervisor,user.admin,user.group_code)
}