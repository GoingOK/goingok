package controllers

import play.api.Logging

trait GoingOkController extends Logging {

  //val logger: Logger = Logger(this.getClass)

  val UNAUTHORIZED_MESSAGE = "You need to login to GoingOK to access this page"

}
