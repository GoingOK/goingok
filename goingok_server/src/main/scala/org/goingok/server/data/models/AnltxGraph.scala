package org.goingok.server.data.models

case class AnltxGraph(
                  graph_id:Int,
                  graph_type:String,
                  name:String,
                  description:String,
                  node_ids:Vector[Int],
                  edge_ids:Vector[Int]
                ) {
  def this(dbGraph: AnltxDBGraph) = this(dbGraph.graph_id, dbGraph.graph_type, dbGraph.name, dbGraph.description, Vector(), Vector())
}
