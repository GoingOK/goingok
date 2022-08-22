package org.goingok.server.data

import org.goingok.server.data.models.{AnltxChart, AnltxEdge, AnltxEdgeLabel, AnltxGraph, GroupCode, AnltxLabel, AnltxNode, AnltxNodeLabel, ReflectionAuthorEntry, ReflectionAuthorEntryAndGroup, ReflectionEntry, Reflection}

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

  case class Supervisors(value:Seq[(String,String,Boolean)]) extends Result

  //case class Reflections(value:Seq[Reflection]) extends Result

  //type Reflections = DBResult[Seq[Reflection]]
  //case class AnltxNodes(value:Seq[AnltxNode]) extends Result

  //type AnltxNodes = DBResult[Seq[AnltxNode]]
  //case class AnltxGraphs(value:Seq[AnltxGraph]) extends Result

  //type AnltxGraphs = DBResult[Seq[AnltxGraph]]

  //case class AnltxGraphNodes(value:Seq[(Int,Int)]) extends Result
  //case class AnltxEdges(value:Seq[Edge]) extends Result
  //case class AnltxCharts(value:Seq[Chart]) extends Result
  //case class AnltxLabels(value:Seq[Label]) extends Result
  //case class AnltxNodeLabels(value:Seq[NodeLabel]) extends Result
  //case class AnltxEdgeLabels(value:Seq[EdgeLabel]) extends Result

}
