package org.goingok.server.data

case class Analytics(
                      userCounts:Seq[(String,Int)],
                      reflectionCounts:Seq[(String,Int)]
                    )