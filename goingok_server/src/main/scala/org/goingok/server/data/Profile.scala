package org.goingok.server.data

import org.goingok.server.data.models.{ReflectionEntry, User}

case class Profile(user:Option[User]=None,reflections:Option[Map[String,Vector[ReflectionEntry]]]=None)