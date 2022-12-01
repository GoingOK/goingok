package views.components.register

import org.goingok.server.data.models.UiMessage
import scalatags.Text.TypedTag
import scalatags.Text.all._

object GroupPopup {



  /** Group required popup message display */
  def display(message:TypedTag[String]) = div(
    p("In order to use GoingOK, you need to register with an existing GoingOK group such as an organisational unit, a subject cohort, or a research group."),
    p("A ",b("GoingOK group code")," should have been given to you by someone responsible for your group."),
    p("Please enter this code below and click ",b("register.")),
    div( `class`:="card-footer text-muted",
      message,
      form( `class`:="needs-validation", action:="/registerWithGroup", method:="POST", attr("novalidate"):="",
        div(`class`:="form-group form-check",
          input(`type`:="checkbox", name:="disclaimer",`class`:="form-check-input",id:="disclaimer",attr("required"):=""),
          label(`class`:="form-check-label", attr("for"):="exampleCheck1",div(`class`:="small",disclaimer)),
          div(`class`:="invalid-feedback",b("You need to read and agree to this statement to continue. If you do not agree, please do not use GoingOK."))
        ),
        div(`class`:="form-group text-center",
          label(`for`:="group-code", `class`:="col-form-label")("GoingOK group code:"),
          input(`type`:="text", name:="group-code", `class`:="form-control", id:="group-code",attr("required"):=""),
          div(`class`:="invalid-feedback","A valid group-code is required in order to use GoingOK.")
        ),
        div(`class`:="form-group text-center",
          input(`type`:="submit", value:="register", `class`:="btn btn-success")
        ),
        script(validationScript)
      )
    )
  )

//  private def showMessage(message:String) = {
//    if(message!="") {
//      div(id:="message",`class`:="alert alert-danger",attr("role"):="alert",message)
//    } else {
//      div()
//    }
//
//  }

  private val disclaimer = raw("""
                             |I have read the <a href="/help">help section</a> on how GoingOK treats my data, and I understand
                             |that I retain ownership over my raw data and that anonymous analytics created from my data
                             |may be retained for computation analysis which may be used for research purposes and to
                             |improve the GoingOK software.
                           """.stripMargin)

  private val validationScript =
    """
      |// Example starter JavaScript for disabling form submissions if there are invalid fields
      |          (function() {
      |          'use strict';
      |  window.addEventListener('load', function() {
      |    // Fetch all the forms we want to apply custom Bootstrap validation styles to
      |    var forms = document.getElementsByClassName('needs-validation');
      |    // Loop over them and prevent submission
      |    var validation = Array.prototype.filter.call(forms, function(form) {
      |      form.addEventListener('submit', function(event) {
      |        if (form.checkValidity() === false) {
      |          event.preventDefault();
      |          event.stopPropagation();
      |        }
      |        form.classList.add('was-validated');
      |      }, false);
      |    });
      |  }, false);
      |  })();
    """.stripMargin

}