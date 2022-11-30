package org.goingok.server.data.models

case class GroupAnalytics(
                           mergedCounts: Seq[(String, Int, Int)],
                           charts:Seq[AnalyticsChartsData]
                           //                           reflections:Vector[GroupAuthorReflection],
                           //                           charts:Vector[AnltxChart],
                           //                           graphs:Vector[AnltxGraph]
                    )