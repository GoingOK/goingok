package views.components

import scalatags.Text.all._

object HelpContent {

  val sjs = div(p(id :="sjs","..."))

  lazy val mainContainer = div(id := "profile-content",`class` := "container-fluid",
    //style := "position: relative;", //attr("data-spy"):="scroll", attr("data-target"):="#help-list",
    div(`class`:="row",
      div(`class`:="col-sm-1"),
      div(`class`:="col-sm-2",
        div(id := "help-list", `class`:="list-group", style := "position: fixed;",
          a(`class`:="list-group-item list-group-item-action", href:="#overview")("Why reflect?"),
          a(`class`:="list-group-item list-group-item-action", href:="#privacy_info")("Privacy"),
          a(`class`:="list-group-item list-group-item-action", href:="#research_info")("Research"),
          a(`class`:="list-group-item list-group-item-action", href:="#using_info")("How to Use"),
          a(`class`:="list-group-item list-group-item-action", href:="#other_info")("Other Info")
        ),
      ),
      div(`class`:="col-sm-8", //style :="height: 300px; overflow-y: scroll",
      div( //attr("data-spy"):="scroll", attr("data-target"):="#help-list", attr("data-offset"):="0",
        div(id:="overview",
          h4("Why write reflectively?"),
          p("Reflective writing can be helpful in working through challenging situations. Writing down your thoughts about recent events helps you organise them and make sense of them. It allows you to take time to process the emotions associated with significant events, but at your own pace and on your own terms. Often significant events are thrust upon you, you do not have the opportunity to choose how you will react. Mostly, in these situations you will react instinctively, intuitively, or in accordance with training or habits developed over time. If you lack experience in dealing with particular challenging situations, your quick decisions may not look as appropriate in hindsight. Reflecting on these situations and the decisions made, will allow you to consider what you might change next time. In other words, reflective writing helps you learn from challenging situations."),
          p("However, writing reflectively is not just about learning. Many studies have shown that personal wellbeing benefits flow from writing about difficult situations. The process of writing allows you to process things that may be difficult to speak about, or that you do not want to dwell on. It can give you an opportunity to express emotions but without the risk of making that expression to someone who may not understand you.")
        ),
        hr,
        div(id:="privacy_info",
          h4("GoingOK cares about your privacy"),
          p("Reflective writing is very personal, and for this reason GoingOK takes privacy of your data very seriously. As a GoingOK user, you are anonymous. By default GoingOK does not store any personal information with your reflective writing. When you log in, we accept that you are a valid user by virtue of you holding an account with Google, but we do not collect any of your Google information. When you first login, you are assigned a unique ID which allows us to ensure that your writing remains yours alone and is there the next time that you login. The text of your reflective writing is your data, and you will always remain the owner of this data. You will remain in control of your data and can choose to use your data how you see fit. You may cancel your account at any time and your data will be deleted from the system when you do. While you keep a GoingOK account, GoingOK may analyse your data for the purposes of providing a range of services including insights on your reflective writing and on the writing of groups of people. However, any analyses are kept separate from your data and cannot be linked back to you. Put simply, you cannot be identified through any of the analyses that GoingOK performs on your data. Some of these analyses may form the basis of computer models, and these models will be owned by GoingOK and may exist independently of your data, and therefore may remain after your account has been deleted."),
          p("Reflecting regularly is particularly beneficial, so you may choose to allow GoingOK to send you a regular reminder. This will require you to nominate an email address that GoingOK can send the reminder to. This email address will be kept separately from your reflective writing to ensure that your privacy is not compromised. You can start and stop this reminder service at any time.")
        ),
        hr,
        div(id:="research_info",
          h4("Helping research"),
          p("GoingOK also provides an opportunity for researchers to better understand people like yourself who are facing similar situations. You may be asked if you are willing to help these research projects by allowing limited access to your de-identified data. That is, if you accept to allowing your de-identified data to be used for research, researchers may be able to read your writing, but they would not know who you are - they would not have access to information that identifies you. You are not under any obligation to participate in any research, but doing so is unlikely to cause you any harm, and participating helps gain a better understanding of people in your situation."),
          p("If you elect for your data to be included in research, it is possible that the researchers may want to contact you about what you have written. GoingOK will never give your personal information to a researcher for this purpose. In such a situation, GoingOK may send you a contact request which you can ignore, decline or accept. If you accept, you can provide an email address that the researcher can contact you on. Researchers will only be able to request contact like this if they have ethical approval from their institution to make contact as part of the research. Although this type of contact will allow the researcher to know that you wrote your reflections, it does not compromise your data in any other ways. Your data still remains yours, and you remain anonymous to all other researchers.")
        ),
        hr,
        div(id:="using_info",
          h4("How to use GoingOK"),
          p("If you're not sure how to use GoingOK, please see our User's Guide or Facilitator's Guide on ", a(href:="http://goingok.org")("GoingOK.org"))

        ),
        hr,
        div(id:="other_info",
          h4("Other Information"),
          p("For further information on GoingOK, visit the ",a(href:="http://goingok.org")("GoingOK.org")," website.")
        ),
        hr,
      ),
      ),
      div(`class`:="col-sm-1")
    )
  )
}



