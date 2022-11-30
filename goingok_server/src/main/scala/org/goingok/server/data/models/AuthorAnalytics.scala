package org.goingok.server.data.models

case class AuthorAnalytics(
                            refs: Vector[ReflectionEntry],
                            graphs: Vector[AnltxGraph],
                            nodes: Vector[AnltxNode],
                            edges: Vector[AnltxEdge],
                            charts: Vector[AnltxChart],
                            nodeLabels: Vector[AnltxNodeLabel],
                            edgeLabels: Vector[AnltxEdgeLabel],
                            labels: Vector[AnltxLabel]
                          )
