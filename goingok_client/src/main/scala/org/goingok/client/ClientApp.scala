package org.goingok.client

import org.querki.jquery._
import org.querki.jsext._

import scala.scalajs.js
import org.scalajs.dom._

import scala.scalajs.js.Dictionary
import scala.scalajs.js.annotation.JSExportTopLevel

object ClientApp {

  //@JSExportTopLevel("auth2")
  //var auth2:js.Object = js.Object


  def main(args: Array[String]): Unit = {

    println("ClientApp has loaded successfully.")
    //println("Loading Google Signin Auth")
    //auth2 = GoogleSignIn.authInit("1049767681335-rvm76el8aspacomur42uch1v0amgca5s.apps.googleusercontent.com")

  }

//  @JSExportTopLevel("signInCallback")
//  def signInCallback(authResult:js.Dictionary[String]) = {
//    val code = authResult.getOrElse("code","")
//    //println(s"Signin callback has been called! Result: $code")
//    //println("sending code to server...")
//    val settings = new JQueryAjaxSettingsBuilder(noOpts)
//      .`type`("POST")
//      .url("http://localhost:9000/authenticate")
//        .headers(Dictionary("X-Requested-With" -> "XMLHttpRequest"))
//        .contentType("text/plain")
//      .success(responseHandler)
//        .processData(false)
//        .data(code)
//
//    $.ajax(settings)
//
//  }
//
//  def responseHandler: (js.Any, String, JQueryXHR) => Unit = (data:js.Any, status:String, jqXHR:JQueryXHR) => {
//    println(s"handling the server response: ${data.toString} | $status | ${jqXHR.toString}")
////    val redirection = data.toString match {
////      case "profile" => "/profile"
////      case "register" => "/register"
////      case _ => "/"
////    }
////    window.location.replace(redirection)
//  }

}

/*
if (authResult['code']) {

    // Hide the sign-in button now that the user is authorized, for example:
    $('#signinButton').attr('style', 'display: none');

    // Send the code to the server
    $.ajax({
      type: 'POST',
      url: 'http://example.com/storeauthcode',
      // Always include an `X-Requested-With` header in every AJAX request,
      // to protect against CSRF attacks.
      headers: {
        'X-Requested-With': 'XMLHttpRequest'
      },
      contentType: 'application/octet-stream; charset=utf-8',
      success: function(result) {
        // Handle or verify the server response.
      },
      processData: false,
      data: authResult['code']
    });
  } else {
    // There was an error.
  }
 */