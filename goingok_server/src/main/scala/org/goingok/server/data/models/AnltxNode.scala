package org.goingok.server.data.models

case class AnltxNode(
               node_id:Int,
               node_type:String,
               node_code:String,
               ref_id:Int,
               start_idx:Int,
               end_idx:Int
               )
