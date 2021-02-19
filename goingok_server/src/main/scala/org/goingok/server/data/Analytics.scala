package org.goingok.server.data

import org.goingok.server.data.models.AnalyticsChartsData

case class Analytics(
                      mergedCounts:Seq[(String,Int,Int)],
                      charts:Seq[AnalyticsChartsData]
                    )