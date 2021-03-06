package org.goingok.server.data

import org.goingok.server.data.models.{GroupCode, ReflectionAuthorEntry, ReflectionAuthorEntryAndGroup, ReflectionEntry}

object DbResults {

  sealed trait Result

  case class GroupedUserCounts(value:Seq[(String,Int)]) extends Result

  case class GroupedReflectionCounts(value:Seq[(String,Int)]) extends Result

  case class GroupedReflections(value:Seq[ReflectionEntry]) extends Result

  case class GroupedAuthorReflections(value:Seq[ReflectionAuthorEntry]) extends Result

  case class GroupedPseudonymCounts(value:Seq[(Option[String],Int)]) extends Result

  case class GroupCodes(value:Seq[GroupCode]) extends Result

  case class Permission(value:String) extends Result

  case class GroupAdmins(value:Seq[(String,String,String)]) extends Result

  case class GroupedAuthorReflectionsByUser(value:Seq[ReflectionAuthorEntryAndGroup]) extends Result

  case class Testers(value:Seq[String]) extends Result
}
