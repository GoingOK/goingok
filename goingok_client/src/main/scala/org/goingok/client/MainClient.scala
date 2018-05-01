package org.goingok.client

import org.goingok.shared.SharedObject
import org.scalajs.dom

object MainClient {

  //As this is a main method it should run on client load
  def main(args: Array[String]): Unit = {
    dom.document.getElementById("sjs").textContent = "This text has been inserted by a scalaJs script"
    println("This is scalaJs printing to the console. The shared message is: "+SharedObject.sharedMessage)
  }

}
