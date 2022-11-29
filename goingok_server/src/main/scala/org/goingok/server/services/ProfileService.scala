package org.goingok.server.services

import java.util.UUID
import com.typesafe.scalalogging.Logger
import org.goingok.server.data.models.{Activity, AnalyticsAuthorChartsData, AnalyticsChartEdge, AnalyticsChartNode, AnltxChart, AnltxEdge, AnltxEdgeLabel, AnltxGraph, AnltxLabel, AnltxNode, AnltxNodeLabel, AuthorAnalytics, AuthorReflection, Reflection, ReflectionData, ReflectionEntry, User}

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

  def getAuthorAnalytics(goingok_ids:Vector[UUID]): Either[Throwable, Map[UUID,AuthorAnalytics]] = {
    //val goingok_id = goingok_ids.head
    //val idMap:Map[UUID,AuthorAnalytics] = goingok_ids.map(g => g->AuthorAnalytics(Vector(),Vector(),Vector(),Vector(),Vector(),Vector(),Vector(),Vector())).toMap
    val (lerrors, ranalytics) = goingok_ids.map{g =>
      getSingleAuthorAnalytics(g)}.partitionMap(identity)
    lerrors.headOption.toLeft(ranalytics.flatten.toMap)
//    for {
//      rs <- ds.getReflectionsforAuthor(goingok_id)
//      gs <- ds.getGraphsforAuthor(goingok_id)
//      ns <- getAllRefNodes(rs)
//      gsn <- addNodesToGraphs(gs)
//      es <- getEdgesForGraphs(gsn)
//      gse <- addEdgeIdsToGraphs(gsn,es)
//      cs <- getChartsForGraphs(gs)
//      nls <- getNodeLabelsForCharts(cs)
//      els <- getEdgeLabelsForCharts(cs)
//      ls <- getLabels(nls,els)
//    } yield AuthorAnalytics(rs,gse,ns,es,cs,nls,els,ls)

  }

  def getSingleAuthorAnalytics(goingok_id:UUID): Either[Throwable, Map[UUID,AuthorAnalytics]] = {

    for {
      rs <- ds.getReflectionsforAuthor(goingok_id)
      gs <- ds.getGraphsforAuthor(goingok_id)
      ns <- getAllRefNodes(rs)
      gsn <- addNodesToGraphs(gs)
      es <- getEdgesForGraphs(gsn)
      gse <- addEdgeIdsToGraphs(gsn, es)
      cs <- getChartsForGraphs(gs)
      nls <- getNodeLabelsForCharts(cs)
      els <- getEdgeLabelsForCharts(cs)
      ls <- getLabels(nls, els)
    } yield Map(goingok_id -> AuthorAnalytics(rs, gse, ns, es, cs, nls, els, ls))

  }

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
    errors.headOption.toLeft(rgraphs)
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

  def getAuthorChartsAnalytics(aaMap: Map[UUID,AuthorAnalytics]): Map[String,Vector[AnalyticsAuthorChartsData]] = {
    aaMap.map { g =>
      Tuple2(g._1.toString, getSingleAuthorChartsAnalytics(g._2))
    }
  }

  def getSingleAuthorChartsAnalytics(authorAnalytics: AuthorAnalytics): Vector[AnalyticsAuthorChartsData] = {
    authorAnalytics.charts.map(aa => AnalyticsAuthorChartsData(
      aa.name,
      aa.description,
      authorAnalytics.nodeLabels.map(nl => {
        val node = authorAnalytics.nodes.find(n => n.node_id == nl.node_id)
        val label = authorAnalytics.labels.find(l => l.label_id == nl.label_id)
        AnalyticsChartNode(
          nl.node_id,
          node.get.node_type,
          node.get.ref_id,
          node.get.start_idx,
          node.get.end_idx,
          nl.expression,
          label.get.label_type,
          label.get.ui_name,
          label.get.description,
          label.get.selected,
          label.get.properties
        )
      }),
      authorAnalytics.edgeLabels.map(el => {
        val edge = authorAnalytics.edges.find(e => e.edge_id == el.edge_id)
        val label = authorAnalytics.labels.find(l => l.label_id == el.label_id)
        AnalyticsChartEdge(
          edge.get.edge_id,
          edge.get.edge_type,
          edge.get.source,
          edge.get.target,
          edge.get.directional,
          edge.get.weight,
          label.get.label_type,
          label.get.ui_name,
          label.get.description,
          label.get.selected,
          label.get.properties
        )
      })
    ))
  }

}
