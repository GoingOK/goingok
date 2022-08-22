package org.goingok.server.data.models

case class Analytics(
                      reflections:Vector[AuthorReflection],
                      charts:Vector[AnltxChart],
                      graphs:Vector[AnltxGraph]
                    )