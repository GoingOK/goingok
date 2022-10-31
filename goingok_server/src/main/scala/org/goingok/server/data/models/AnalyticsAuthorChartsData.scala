package org.goingok.server.data.models

case class AnalyticsAuthorChartsData(name: String, description: String, nodes: Vector[AnalyticsChartNode], edges: Vector[AnalyticsChartEdge])

case class AnalyticsChartNode(
                               id: Int,
                               nodeType: String,
                               refId: Int,
                               startIdx: Int,
                               endIdx: Int,
                               expression:String,
                               labelType: String,
                               name: String,
                               description: String,
                               selected: Boolean,
                               properties: ujson.Obj
                             ) {
  def this(node: AnltxNode, nodeLabel: AnltxNodeLabel, label: AnltxLabel) = this(
    node.node_id,
    node.node_type,
    node.ref_id,
    node.start_idx,
    node.end_idx,
    nodeLabel.expression,
    label.label_type,
    label.ui_name,
    label.description,
    label.selected,
    label.properties
  )
}

case class AnalyticsChartEdge(
                               id: Int,
                               edgeType: String,
                               source: Int,
                               target: Int,
                               directional: Boolean,
                               weight: Double,
                               labelType: String,
                               name: String,
                               description: String,
                               selected: Boolean,
                               properties: ujson.Obj
                             ) {
  def this(edge: AnltxEdge, label: AnltxLabel) = this(
    edge.edge_id,
    edge.edge_type,
    edge.source,
    edge.target,
    edge.directional,
    edge.weight,
    label.label_type,
    label.ui_name,
    label.description,
    label.selected,
    label.properties
  )
}