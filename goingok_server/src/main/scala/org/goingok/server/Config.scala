package org.goingok.server

import com.sun.org.apache.xerces.internal.xs.StringList
import com.typesafe.config.ConfigFactory

import scala.util.Try

object Config {

  private val config = ConfigFactory.load()
  def string(path:String):String = config.getString(path)
  def stringOpt(path:String):Option[String] = Try(config.getString(path)).toOption
  def int(path:String):Int = config.getInt(path)

  lazy val baseUrl:Option[String] = stringOpt("app.baseurl")
  lazy val dbUrl:Option[String] = stringOpt("db.url")
  lazy val googleClientSecret:Option[String] = stringOpt("google.client.secret")
  lazy val goingokSecret:Option[String] = stringOpt("play.http.secret.key")
  lazy val googleRedirectUrl:Option[String] = stringOpt("google.redirect.url")
  lazy val googleClientId:Option[String] = stringOpt("google.client.id")
  lazy val dbUser:Option[String] = stringOpt("db.user")
  lazy val dbSecret:Option[String] = stringOpt("db.password")
  lazy val awsCognitoAppClientId:Option[String] = stringOpt("aws.cognito.app_client_id")
  lazy val awsCognitoAppClientSecret:Option[String] = stringOpt("aws.cognito.app_client_secret")
  lazy val awsCognitoAuthUrl:Option[String] = stringOpt("aws.cognito.auth_url")
  lazy val awsCognitoRedirectUrl:Option[String] = stringOpt("aws.cognito.redirect_url")
}