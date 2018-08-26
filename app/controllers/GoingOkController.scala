package controllers

import play.api.Logger

trait GoingOkController {

  val logger: Logger = Logger(this.getClass)

  val UNAUTHORIZED_MESSAGE = "You need to login to GoingOK to access this page"

}
