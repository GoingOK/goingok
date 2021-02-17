package org.goingok.server.data

import org.goingok.server.data.models.AnalyticsChartsData

case class Analytics(
                      userCounts:Seq[(String,Int)],
                      reflectionCounts:Seq[(String,Int)],
                      chartData:Seq[AnalyticsChartsData]
                    )