package org.goingok.server.services

import java.util.UUID
import com.typesafe.scalalogging.Logger
import org.goingok.server.data.models.{Activity, AnltxChart, AnltxEdge, AnltxEdgeLabel, AnltxGraph, AnltxLabel, AnltxNode, AnltxNodeLabel, AuthorAnalytics, AuthorReflection, Reflection, ReflectionData, ReflectionEntry, User, UserPseudonym}

import java.time.LocalDateTime

class ProfileService {

  val logger = Logger(this.getClass)

  val ds = new DataService

  /**
    * Gets all reflections for a given user
    * @param goingok_id GoingOK user ID
    * @return Vector of reflections
    */
//  def getReflections(goingok_id:UUID): Option[Vector[ReflectionEntry]] = {
//    ds.getReflectionsForUser(goingok_id) match {
//      case Right(refs) => Some(refs)
//      case Left(error) => {
//        logger.error(s"There was a problem getting reflections for $goingok_id: ${error.getMessage}")
//        None
//      }
//    }
//  }

//  def getAuthorReflections(goingok_ids:List[UUID]): Either[Throwable,Vector[Reflection]] = {
//    ds.getReflectionsforAuthor(goingok_ids.head)
//  }

  /**
    * Get GoingOK user from DB by GoingOK user ID
    * @param goingok_id GoingOK user ID
    * @return GoingOK user
    */
  def getUser(goingok_id:UUID): Option[User] = {
    ds.getUserForId(goingok_id) match {
      case Right(user) => Some(user)
      case Left(error) => {
        logger.error(s"There was a problem getting the user for $goingok_id: ${error.getMessage}")
        None
      }
    }
  }

  /**
    * Saves Reflection
    * @param reflection Reflection
    * @param goingok_id GoingOK user ID
    */
  def saveReflection(reflection:ReflectionData,goingok_id:UUID):Either[Throwable,Int] = for {
    rows <- ds.insertReflection(reflection:ReflectionData,goingok_id:UUID)
  } yield rows

  def getAuthorAnalytics(goingok_ids:Vector[UserPseudonym]): Either[Throwable, Map[UserPseudonym,AuthorAnalytics]] = {
    logger.debug(s"getAuthorAnalytics input: $goingok_ids")
    val (lerrors, ranalytics) = goingok_ids.map{up =>
      getSingleAuthorAnalytics(up)}.partitionMap(identity)
    lerrors.headOption.toLeft(ranalytics.flatten.toMap)
  }

  private def getSingleAuthorAnalytics(userp:UserPseudonym): Either[Throwable, Map[UserPseudonym,AuthorAnalytics]] = {
    logger.debug(s"getSingleAuthorAnalytics input: $userp")
    val result = for {
      rs <- ds.getReflectionsforAuthor(userp.goingok_id)
      gs <- ds.getGraphsforAuthor(userp.goingok_id)
      ns <- getAllRefNodes(rs)
      gsn <- addNodesToGraphs(gs)
      es <- getEdgesForGraphs(gsn)
      gse <- addEdgeIdsToGraphs(gsn, es)
      cs <- getChartsForGraphs(gs)
      nls <- getNodeLabelsForCharts(cs)
      els <- getEdgeLabelsForCharts(cs)
      ls <- getLabels(nls, els)
    } yield Map(userp -> AuthorAnalytics(rs, gse, ns, es, cs, nls, els, ls))
    logger.debug(s"getSingleAuthorAnalytics result: $result")
    result
  }

  def getAssociatedIds(goingok_id:UUID): Either[Throwable, Vector[UserPseudonym]] = {
    val result = for {
      us <- ds.getAssociatedIdsWithPseudonyms(goingok_id)
      //ps <- getAllPseudonyms(us)
    } yield us  //ps
    result match {
      case Right(r) => logger.debug(s"getAssociatedIds result:${r.toString}")
      case Left(e) => logger.error(s"getAssociatedIds error:${e.getMessage}")
    }
    result
  }

//  private def getAllPseudonyms(uuids:Vector[UUID]): Either[Throwable, Vector[(UUID,String)]] = {
//
//  }

  private def getAllRefNodes(reflections:Vector[Reflection]) = {
    val (lnodes, rnodes) = reflections.map(r => ds.getNodesForReflection(r.ref_id)).partitionMap(identity)
    lnodes.headOption.toLeft(rnodes.flatten[AnltxNode])
  }

  private def addNodesToGraphs(graphs:Vector[AnltxGraph]) = {
    val (errors, rgraphs) = graphs.map(g => ds.getNodesForGraph(g.graph_id).map(n => g.copy(node_ids = n))).partitionMap(identity)
    errors.headOption.toLeft(rgraphs)
  }

  private def getEdgesForGraphs(graphs:Vector[AnltxGraph]) = {
    val (errors,rgraphs) = graphs.map(g => ds.getEdgesForGraph(g.graph_id)).partitionMap(identity)
    errors.headOption.toLeft(rgraphs.flatten)
  }

  private def getChartsForGraphs(graphs: Vector[AnltxGraph]) = {
    val (errors, rgraphs) = graphs.map(g => ds.getChartForGraph(g.graph_id)).partitionMap(identity)
    errors.headOption.toLeft(rgraphs.flatten)
  }

  private def getNodeLabelsForCharts(charts: Vector[AnltxChart]) = {
    val (errors, rlabels) = charts.map(c => ds.getNodeLabelsForChart(c.chart_id)).partitionMap(identity)
    errors.headOption.toLeft(rlabels.flatten)
  }

  private def getEdgeLabelsForCharts(charts: Vector[AnltxChart]) = {
    val (errors, rlabels) = charts.map(c => ds.getEdgeLabelsForChart(c.chart_id)).partitionMap(identity)
    errors.headOption.toLeft(rlabels.flatten)
  }

  private def addEdgeIdsToGraphs(graphs:Vector[AnltxGraph], edges:Vector[AnltxEdge]) = Right{
    graphs.map { graph =>
      val gedges = edges.filter(_.graph_id==graph.graph_id).map(_.edge_id)
      graph.copy(edge_ids = gedges)
    }
  }

  private def getLabels(nodeLabels:Vector[AnltxNodeLabel],edgeLabels:Vector[AnltxEdgeLabel]) = {
    val labelIds = nodeLabels.map(_.label_id) ++ edgeLabels.map(_.label_id)
    ds.getLabelsForLabelIds(labelIds)
  }
}
