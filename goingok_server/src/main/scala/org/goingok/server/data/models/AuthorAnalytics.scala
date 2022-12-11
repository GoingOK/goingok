package org.goingok.server.data.models

case class AuthorAnalytics(
                            refs: Vector[ReflectionEntry] = Vector(),
                            graphs: Vector[AnltxGraph] = Vector(),
                            nodes: Vector[AnltxNode] = Vector(),
                            edges: Vector[AnltxEdge] = Vector(),
                            charts: Vector[AnltxChart] = Vector(),
                            nodeLabels: Vector[AnltxNodeLabel] = Vector(),
                            edgeLabels: Vector[AnltxEdgeLabel] = Vector(),
                            labels: Vector[AnltxLabel] = Vector()
                          )
