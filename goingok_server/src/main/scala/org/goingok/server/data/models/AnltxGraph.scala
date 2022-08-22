package org.goingok.server.data.models

case class AnltxGraph(
                  graph_id:Int,
                  graph_type:String,
                  name:String,
                  description:String,
                  node_ids:Vector[Int],
                  edge_ids:Vector[Int]
                ) {
  def this(
            graph_id: Int,
            graph_type: String,
            name: String,
            description: String
          ) = this(graph_id, graph_type, name, description, Vector(), Vector())
}
