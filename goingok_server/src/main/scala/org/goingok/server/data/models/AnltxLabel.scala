package org.goingok.server.data.models

case class AnltxLabel(
                label_id:Int,
                label_type:String,
                ui_name:String,
                description:String,
                selected:Boolean,
                properties:ujson.Obj
                )
