package org.goingok.server.data.models

//import org.goingok.server.data.unused.UserPseudonym

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

  //Build an Author from a UserPseudonym (converts pseudonym to Option with none if empty
  //def this (userp: UserPseudonym) = this(userp.goingok_id,Option(userp.pseudonym).filter(_.trim.nonEmpty))
}