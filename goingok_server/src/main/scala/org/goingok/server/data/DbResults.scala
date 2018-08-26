package org.goingok.server.data

import org.goingok.server.data.models.ReflectionEntry

object DbResults {

  sealed trait Result

  case class GroupedUserCounts(value:Seq[(String,Int)]) extends Result

  case class GroupedReflectionCounts(value:Seq[(String,Int)]) extends Result

  case class GroupedReflections(value:Seq[ReflectionEntry]) extends Result

  case class Permission(value:String) extends Result
}
