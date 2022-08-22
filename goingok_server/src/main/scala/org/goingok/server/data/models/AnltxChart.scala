package org.goingok.server.data.models

case class AnltxChart(
                       chart_id:Int,
                       graph_id:Int,
                       name:String,
                       description:String,
                       node_labels:Vector[AnltxNodeLabel],
                       edge_labels:Vector[AnltxEdgeLabel],
                       labels:Vector[AnltxLabel]
                ) {
    def this(dbchart:AnltxDBChart) = this(dbchart.chart_id,dbchart.graph_id,dbchart.name,dbchart.description,Vector(),Vector(),Vector())
  }