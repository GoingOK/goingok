package views.components.profile

import scalatags.Text.all._

object ReflectionEntry {

  def display(sliderPoint:Double=0.0) = form( `class` := "needs-validation", action:="/profile", method:="POST", attr("novalidate"):="",
    div( `class` := "row",
      div( `class` :="col",
        this.slider(sliderPoint)
      )
    ),
    hr(),
    div(`class`:="form-group",
      label(attr("for"):="describe","Describe how you are going..."),
      textarea(`class`:="form-control", id := "describe", name := "reflection-text", rows := "10", attr("required"):=""),
      div(`class`:="invalid-feedback",b("Please write something about how you are going.")),
    ),
    div(`class`:="form-group",
      input(`type`:="submit", value:="save", `class`:="btn btn-success")
    ),
    script(raw(validationScript))
  )

  def demoDisplay(sliderPoint:Double=0.0) = form( `class` := "needs-validation", action:="/profile", method:="POST", attr("novalidate"):="",
    div( `class` := "row",
      div( `class` :="col",
        this.slider(sliderPoint)
      )
    ),
    hr(),
    div(`class`:="form-group",
      label(attr("for"):="describe","Describe how you are going..."),
      textarea(`class`:="form-control", id := "describe", name := "reflection-text", rows := "10", attr("required"):=""),
      div(`class`:="invalid-feedback",b("Please write something about how you are going.")),
    ),
    div(`class`:="form-group",
      input(`type`:="submit", value:="save disabled in demo", `class`:="btn btn-success", disabled)
    ),
    //script(raw(validationScript))
  )

  private def slider(point:Double) = div(id:="slider-group",`class`:="form-group",
    div(`class`:="d-flex justify-content-between",
      div( `class` := "p-2", "Distressed"),
      div( `class` := "p-2", "GoingOK"),
      div( `class` := "p-2", "Soaring")
    ),
    input(`type`:="range",`class`:="form-control-range is-invalid", name:="reflection-point",id:="reflection-point",onchange:="rangeChange()"),
    div(`class`:="invalid-feedback",b("Please set the slider to a point the best describes how you are going.")),
    script(raw(
      """
      """.stripMargin))
  )

  private val validationScript =
    """
      |  (function() {
      |      'use strict';
      |
      |  window.addEventListener('load', function() {
      |
      |    // Fetch all the forms we want to apply custom Bootstrap validation styles to
      |    var forms = document.getElementsByClassName('needs-validation');
      |    // Loop over them and prevent submission
      |    var validation = Array.prototype.filter.call(forms, function(form) {
      |      var slider = document.getElementById('slider-group');
      |      form.addEventListener('submit', function(event) {
      |        if ((form.checkValidity() === false) || rangestatic===true) {
      |          //console.log("rangestatic: ",rangestatic);
      |          slider.style.border = "thin solid #BB0000";
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
