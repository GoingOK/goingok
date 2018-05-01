package org.goingok.data.models

/**
  * Created by andrew@andrewresearch.net on 21/2/17.
  */

case class ResponseMessage(message:String)

object ResponseMessage {
  def apply(message:Boolean) = new ResponseMessage(message.toString)
}