package org.goingok.server.data.models

import java.util.UUID

case class GroupAuthorReflection(goingok_id:UUID, refs:Vector[ReflectionDB])
