package org.goingok.server.data

import org.goingok.server.data.models.{Author, AuthorAnalytics}

case class AuthorProfile(
                        author:Option[Author]=None,
                        analytics:Option[AuthorAnalytics] = None
                      )