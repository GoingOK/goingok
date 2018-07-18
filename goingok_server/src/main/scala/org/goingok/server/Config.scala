package org.goingok.server

import com.typesafe.config.ConfigFactory

object Config {

  private val config = ConfigFactory.load()
  def string(path:String):String = config.getString(path)
  def int(path:String):Int = config.getInt(path)
}