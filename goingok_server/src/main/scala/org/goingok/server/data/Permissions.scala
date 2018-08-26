package org.goingok.server.data

object Permissions {

  sealed trait Permission {
    val value:String = ""
    def matches(checkVal:String) = checkVal.contentEquals(value)
  }

  object Sensitive extends Permission {
    override val value:String = "SENSITIVE"
  }

}
