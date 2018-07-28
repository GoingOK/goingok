package org.goingok.server

import com.typesafe.config.ConfigFactory

import scala.util.Try

object Config {

  private val config = ConfigFactory.load()
  def string(path:String):String = config.getString(path)
  def stringOpt(path:String):Option[String] = Try(config.getString(path)).toOption
  def int(path:String):Int = config.getInt(path)

  lazy val baseUrl:Option[String] = stringOpt("app.baseurl")
}