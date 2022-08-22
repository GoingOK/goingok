package org.goingok.server.data.models

case class AnltxEdge(
               edge_id:Int,
               graph_id:Int,
               edge_type:String,
               source:Int,
               target:Int,
               directional:Boolean,
               weight:Double
               )
