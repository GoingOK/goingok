package org.goingok.server.data.models

import java.util.UUID

case class AuthorReflection(goingok_id:UUID, refs:Vector[Reflection])
