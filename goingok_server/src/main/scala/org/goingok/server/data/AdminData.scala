package org.goingok.server.data

case class AdminData(
                      groupInfo:Seq[(String,Int)],
                      userInfo:Seq[(String,Int)]
                    )
