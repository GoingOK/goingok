package org.goingok.server.data

case class AdminData(
                      groupInfo:Seq[(String,Int)],
                      groupAdminInfo:Seq[(String,String,String)],
                      userInfo:Seq[(String,Int)]
                    )
