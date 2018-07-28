package org.goingok.server.services

import java.util.UUID

import com.typesafe.scalalogging.Logger
import io.nlytx.commons.privacy.Anonymiser
import org.goingok.server.data.models.User


class AdminService {

  val logger: Logger = Logger(this.getClass)

  val ds = new DataService()

  def createPseudonyms(num:Int): Either[Throwable,Int] = {
    for {
      ss <- ds.getAllPseudonyms.map(_.toSet)
      ns = Anonymiser.generate(num,ss)
      dif = ns.diff(ss)
      res <- ds.insertPseudonyms(dif.toList)
    } yield res
  }

  def getAdminUser(goingok_id:UUID): Either[Throwable, User] = {
    for {
      u <- ds.getUserForId(goingok_id)
      usr <- getAdminUser(u)
    } yield usr
  }


  private def getAdminUser(user:User) = if(user.admin) {
    Right(user)
  } else {
    Left(new Exception("User is not an administrator."))
  }


}
