package org.goingok.server.data.models

import play.api.libs.json.{Json, Writes}

//import org.json4s.JsonAST.JValue

/**
  * Created by andrew@andrewresearch.net on 3/3/17.
  */







case class Research(project:ResearchEntry = ResearchEntry(), organisation:ResearchEntry = ResearchEntry(), cohort:ResearchEntry = ResearchEntry(), consent:Boolean = false)

case class ResearchEntry(name:String = "None Selected", code:String = "NON")








