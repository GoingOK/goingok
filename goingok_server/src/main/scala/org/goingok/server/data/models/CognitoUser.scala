package org.goingok.server.data.models

import java.util.UUID

case class CognitoUser(
                        id:UUID,
                        googleId:Option[String] = None
                      )
