package org.goingok.server.services

import java.time.LocalDateTime

import cats.effect.IO
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import java.util.UUID
import doobie.postgres._
import doobie.postgres.implicits._

import com.typesafe.scalalogging.Logger
import org.goingok.server.Config
import org.goingok.server.data.DbResults
import org.goingok.server.data.models._



//noinspection ScalaStyle
class DataService {

  val logger = Logger(this.getClass)

  private lazy val driver = Config.string("db.driver")
  private lazy val url = Config.string("db.url")
  private lazy val user = Config.string("db.user")
  private lazy val password = Config.string("db.password")

  // We need a ContextShift[IO] before we can construct a Transactor[IO]. The passed ExecutionContext
  // is where nonblocking operations will be executed. For testing here we're using a synchronous EC.
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  private lazy val db = Transactor.fromDriverManager[IO](driver,url,user,password)

  //private implicit val uuidImplicit:AdvancedMeta[UUID] =  doobie.postgres.implicits.UuidType

  type DBResult[T] = Either[Throwable,T]
  def runQuery[A](query:ConnectionIO[A]):DBResult[A] = query.transact(db).attempt.unsafeRunSync

  /**
    * Gets GoingOK user from DB by Google user ID
    * @param googleId Google user ID
    * @return GoingOK user
    */
  def getUserWithGoogleId(googleId:String):Either[Throwable,User] = {
    val query = sql"""select * from users u, user_auths a
                      where u.goingok_id = a.goingok_id
                      and a.google_id = $googleId
                   """.query[User].unique

    runQuery(query)
  }

  /**
    * Gets GoingOK user from DB by Cognito user ID
    * @param cognitoId Cognito user ID
    * @return GoingOK user
    */
  def getUserWithServiceId(serviceUuid:UUID):Either[Throwable,User] = {
    val serviceId = serviceUuid.toString
    val query = sql"""select * from users u, user_auths a
                      where u.goingok_id = a.goingok_id
                      and a.google_id = $serviceId
                   """.query[User].unique

    runQuery(query)
  }

  /**
    * Inserts new user Authentication into DB
    * @param userauth User authentication
    */
  def insertNewUserAuth(userauth:UserAuth):Either[Throwable,UUID] = {
    val query = sql"""insert into user_auths (goingok_id, google_id, google_email, init_timestamp)
                      values(${userauth.goingok_id},${userauth.service_id},${userauth.service_email},${userauth.init_timestamp})
                   """.update
                  .withUniqueGeneratedKeys[UUID]("goingok_id")

    runQuery(query)
  }

  /**
    * Inserts new GoingOK user into DB
    * @param user GoingOK user
    */
  def insertNewUser(user:User):Either[Throwable,UUID] = {
    logger.info(s"Inserting new user: $user")
    val query = sql"""insert into users (goingok_id,pseudonym,research_code,research_consent,supervisor,admin,group_code,register_timestamp)
                      values(${user.goingok_id},${user.pseudonym},
                            ${user.research_code},${user.research_consent},
                            ${user.supervisor},${user.admin},
                            ${user.group_code},${user.register_timestamp}
                            )
                   """.update
                  .withUniqueGeneratedKeys[UUID]("goingok_id")

    runQuery(query)
  }

  /**
    * Gets GoingOK user from DB by GoingOK user ID
    * @param goingok_id GoingOK user ID
    * @return GoingOK user
    */
  def getUserForId(goingok_id:UUID):Either[Throwable,User] = {
    logger.info(s"Getting user for goingok_id: $goingok_id")
    val query = sql"""select * from users
                 where goingok_id = $goingok_id::uuid
              """.query[User].unique

    runQuery(query)
  }

  /**
    * Gets all reflections for a given user by GoingOK user ID
    * @param goingokId GoingOK user ID
    * @return Vector of reflections
    */
  def getReflectionsForUser(goingokId:UUID): Either[Throwable,Vector[ReflectionEntry]] = {
    val query = sql"""select ref_Id, timestamp, point, text
                  from reflections
                  where goingok_id=$goingokId
                  order by timestamp desc""".query[ReflectionEntry]

    runQuery(query.to[Vector])
  }

  /**
    * Gets permission from DB
    * @param goingok_id GoingOK user ID
    * @param group_code GoingOK group code
    * @return permission
    */
  def getPermission(goingok_id:UUID,group_code:String): Either[Throwable,DbResults.Result] = {
    val query = sql"""select permission
                      from group_permissions
                      where goingok_id=$goingok_id::uuid
                      and group_code=$group_code""".query[String].unique
    runQuery(query).map(r => DbResults.Permission(r))
  }

  /**
    *
    */

  def getAllSupervisorUsers(): Either[Throwable,DbResults.Result] = {
    val query = sql"""select pseudonym, group_code, supervisor
                  from users
                  where supervisor=true
                  order by group_code""".query[(String,String,Boolean)]

    runQuery(query.to[Vector]).map(r => DbResults.Supervisors(r))
  }

  /**
    * Gets all reflections for a given group by GoingOK group code
    * @param group_code GoingOK group code
    * @return Sequence of group reflections
    */
  def getReflectionsForGroup(group_code:String): Either[Throwable,DbResults.Result] = {
    val query = sql"""select timestamp, point, text
                  from reflections r, users u
                  where r.goingok_id = u.goingok_id
                  and u.group_code=$group_code""".query[ReflectionEntry]

    runQuery(query.to[Vector]).map(r => DbResults.GroupedReflections(r))
  }

  /**
    * Gets a specified range of reflections for a given group by GoingOK group code
    * @param group_code GoingOK group code
    * @param start range start
    * @param end range end
    * @return Sequence of reflections
    */
  def getReflectionsForGroupWithRange(group_code:String,start:String,end:String): Either[Throwable,DbResults.Result] = {
    val query = sql"""select timestamp, point, text
                  from reflections r, users u
                  where r.goingok_id = u.goingok_id
                  and u.group_code=$group_code
                  and timestamp >=$start
                  and timestamp <=$end""".query[ReflectionEntry]
    runQuery(query.to[Vector]).map(r => DbResults.GroupedReflections(r))
  }

  def getAuthorReflectionsForGroup(group_code:String): Either[Throwable,DbResults.Result] = {
    val query = sql"""select r.timestamp, u.pseudonym, r.point, r.text
                  from reflections r, users u
                  where r.goingok_id = u.goingok_id
                  and u.group_code=$group_code""".query[ReflectionAuthorEntry]

    runQuery(query.to[Vector]).map(r => DbResults.GroupedAuthorReflections(r))
  }

  def getAuthorReflectionsForGroupWithRange(group_code:String,start:String,end:String): Either[Throwable,DbResults.Result] = {
    val query = sql"""select r.timestamp, u.pseudonym, r.point, r.text
                  from reflections r, users u
                  where r.goingok_id = u.goingok_id
                  and u.group_code=$group_code
                  and timestamp >=$start
                  and timestamp <=$end""".query[ReflectionAuthorEntry]
    runQuery(query.to[Vector]).map(r => DbResults.GroupedAuthorReflections(r))
  }

  def getAuthorReflectionsAndGroup(goingok_id:UUID): Either[Throwable,DbResults.Result] = {
    val query = sql"""select u.group_code, gc.created_timestamp, u.pseudonym, r.ref_Id, r.timestamp, r.point, r.text
                  from users u,reflections r, group_codes gc
                  where u.goingok_id = r.goingok_id
                    and u.group_code = gc.group_code
                    and u.group_code in (
                      select p.group_code
                      from group_permissions p
                      where p.goingok_id=$goingok_id)
                  order by  gc.created_timestamp desc""".query[ReflectionAuthorEntryAndGroup]
    runQuery(query.to[Vector]).map(r => DbResults.GroupedAuthorReflectionsByUser(r))
  }

  /**
    * Gets GoingOK group code from DB
    * @param code GoingOK group code
    * @return GoingOK group code
    */
  def getGroupCode(code:String): Either[Throwable,GroupCode] = {
    val query = sql"""select * from group_codes
                      where group_code=$code""".query[GroupCode].unique
    runQuery(query)
  }

  /**
    * Gets all group codes from DB
    * @return Sequence of group codes
    */
  def getAllGroupCodes: Either[Throwable,DbResults.Result] = {
    val query =
      sql"""select * from group_codes
            order by group_code""".query[GroupCode]
    runQuery(query.to[Vector]).map(r => DbResults.GroupCodes(r))
  }

  /**
    * Updates GoingOK group code for a given GoingOK user
    * @param code new GoingOK group code
    * @param goingok_id GoingOK user ID
    */
  def updateCodeForUser(code:String,goingok_id:UUID): Either[Throwable,Int] = {
    val time = LocalDateTime.now().toString
    val query = sql"""update users
                    set group_code=$code, register_timestamp=$time
                    where goingok_id=$goingok_id
                """.update.run

    runQuery(query)
  }

  /**
   * Updates supervisor boolean for a given GoingOK user
   * @param pseudonym GoingOK user pseudonym
   * @param supervisor new supervisor value
   */
  def updateSupervisorForUser(pseudonym: String, supervisor: Boolean): Either[Throwable,Int] = {
    val time = LocalDateTime.now().toString
    val query = sql"""update users
                    set supervisor=$supervisor, register_timestamp=$time
                    where pseudonym=$pseudonym
                """.update.run

    runQuery(query)
  }

  /**
   * Inserts anew tester into DB
   * @param pseudonym GoingOK user pseudonym
   */
  def insertTester(pseudonym: String,scope: String): Either[Throwable,Int] = {
    val query = sql"""insert into testers (pseudonym, scope)
                    values ($pseudonym, $scope)
                """.update.run

    runQuery(query)
  }

  /**
    * Inserts new reflection into DB
    * @param reflection new reflection
    * @param goingok_id GoingOK user ID
    */
  def insertReflection(reflection:ReflectionData,goingok_id:UUID): Either[Throwable,Int] = {
    val time = LocalDateTime.now().toString
    val query = sql"""insert into reflections (timestamp, point, text, goingok_id)
                    values ($time,${reflection.point},${reflection.text},$goingok_id)
                """.update.run

    runQuery(query)
  }

  /**
    * Inserts new activity into DB
    * @param activity new activity
    */
  def insertActivity(activity:Activity): Either[Throwable,Int] = {
    val query = sql"""insert into user_activities (timestamp, goingok_id, actitvity_type, activity_detail)
                    values (${activity.timestamp},${activity.goingok_id},${activity.activity_type},${activity.activity_detail})
                """.update.run

    runQuery(query)
  }

  /**
    * Inserts new group code into DB
    * @param groupCode new group code
    * @param goingok_id GoingOK user ID
    */
  def insertGroup(groupCode:String,goingok_id:UUID): Either[Throwable,Int] = {
    val time = LocalDateTime.now().toString
    val query = sql"""insert into group_codes (group_code, created_timestamp, owner_goingok_id)
                    values ($groupCode,$time,$goingok_id)
                """.update.run
    runQuery(query)
  }

  /**
    * Gets pseudonym counts from DB
    * @return Sequence of pseudonyms
    */
  def getPseudonymCounts: Either[Throwable,DbResults.Result] = {
    val query = sql"""select allocated,count(pseudonym) from pseudonyms
                      group by allocated
                      """.query[(Option[String],Int)]
    runQuery(query.to[Vector]).map(r => DbResults.GroupedPseudonymCounts(r))
  }

  /**
    * Gets all pseudonyms from DB
    * @return Vector of pseudonyms
    */
  def getAllPseudonyms: Either[Throwable,Vector[String]] = {
    val query = sql"""select pseudonym from pseudonyms""".query[String]
    runQuery(query.to[Vector])
  }


  /**
    * Inserts new pseudonyms into DB
    * @param pseudocodes new list of pseudonyms
    */


  def insertPseudonyms(pseudocodes:List[String]): Either[Throwable, Int] = {
    import cats.implicits._
    val query = """insert into pseudonyms (pseudonym) values (?)"""
    val batch = doobie.Update[String](query).updateMany(pseudocodes)
    runQuery(batch)
  }

  /**
    * Gets the next avaliable pseudonym
    * @return pseudonym
    */
  def getNextPseudonym: Either[Throwable,String] = {
    val query = sql"""select pseudonym from pseudonyms where allocated is not true limit 1""".query[String].unique
    runQuery(query)
  }

  /**
    * Update a given pseudonym from 'not allocated' to 'allocated'
    * @param pseudonym as string
    * @return
    */
  def updatePseudonym(pseudonym:String): Either[Throwable,Int] = {
    val query = sql"""update pseudonyms set allocated = true where pseudonym = $pseudonym""".update.run
    runQuery(query)
  }

  /**
    * Counts all existing GoingOk users
    * @return Sequence of users
    */
  def countUsers:Either[Throwable,DbResults.Result] = {
    val query = sql"""SELECT group_code, COUNT(*)
                      FROM users
                      GROUP BY group_code
                      ORDER BY group_code
      """.query[(String,Int)]
    runQuery(query.to[Vector]).map(r => DbResults.GroupedUserCounts(r))
  }

  /**
    * Counts all existing reflections
    * @param goingok_id GoingOk user ID
    * @return Sequence of reflections
    */
  def countReflections(goingok_id:UUID):Either[Throwable,DbResults.Result] = {
    val query = sql"""select u.group_code, count(*)
                     from users u,reflections r
                     where u.goingok_id = r.goingok_id
                     and u.group_code in (
                      select p.group_code
                      from group_permissions p
                      where p.goingok_id=$goingok_id
                     )
                     group by u.group_code
                     order by u.group_code
      """.query[(String, Int)]
    runQuery(query.to[Vector]).map(r => DbResults.GroupedReflectionCounts(r))
  }

  def getGroupAdmins():Either[Throwable,DbResults.Result] = {
    val query =
      sql"""select p.group_code, u.pseudonym ,a.google_email
            from group_permissions p, user_auths a, users u
            where p.goingok_id = a.goingok_id
            and a.goingok_id = u.goingok_id
            and permission = 'SENSITIVE'
            order by a.google_email;
           """.query[(String,String,String)]
    runQuery(query.to[Vector]).map(r => DbResults.GroupAdmins(r))
  }

  def getTesters():Either[Throwable,DbResults.Result] = {
    val query =
      sql"""select pseudonym
           from testers""".query[String]
    runQuery(query.to[Vector]).map(r => DbResults.Testers(r))
  }

  def getGoingokIdForPseudonym(pseudonym:String):Either[Throwable,UUID] = {
    val query = sql"""select goingok_id from users
                      where pseudonym = $pseudonym
                   """.query[UUID].unique

    runQuery(query)
  }
  def insertGroupAdmin(goingok_id:UUID,group_code:String,permission:String):Either[Throwable,Int] = {
    val query = sql"""insert into group_permissions (goingok_id, group_code, permission)
                    values ($goingok_id,$group_code,$permission)
                """.update.run
    runQuery(query)
  }

  //---------------------------------------------------------------------------------------------------------
  // Analytics functions

  def getReflectionsforAuthor(goingok_id:UUID): DBResult[Vector[Reflection]] = {
    val query =
      sql"""select r.ref_id, r.timestamp, r.text, r.point
            from reflections r
            where goingok_id = $goingok_id
            order by r.timestamp desc;""".query[Reflection]
    runQuery(query.to[Vector])
  }
  def getGraphsforAuthor(goingok_id: UUID): DBResult[Vector[AnltxGraph]] = {
    val query =
      sql"""select g.graph_id, g.graph_type, g.name, g.description
            from anltx_graphs g, anltx_author_graphs a
            where a.goingok_id = $goingok_id
            and a.graph_id = g.graph_id;
           """.query[AnltxDBGraph]
    val result = runQuery(query.to[Vector])
    result.map(c => c.map( d => new AnltxGraph(d)))
  }
  def getNodesForGraph(graph_id:Int): DBResult[Vector[Int]] = {
    val query =
      sql"""select node_id
           from anltx_graph_nodes
           where graph_id = $graph_id""".query[Int]
   runQuery(query.to[Vector])
  }

  def getEdgesForGraph(graph_id:Int): DBResult[Vector[AnltxEdge]] = {
    val query =
      sql"""select e.edge_id, e.graph_id, e.edge_type, e.source_node_id, e.target_node_id, e.directional, e.weight
           from anltx_edges e
           where e.graph_id = $graph_id""".query[AnltxEdge]
    runQuery(query.to[Vector])
  }
  def getNodesForReflection(ref_id:Int): DBResult[Vector[AnltxNode]] = {
    val query =
      sql"""select n.node_id, n.node_type, n.ref_id, n.start_idx, n.end_idx
            from anltx_nodes n
            where n.ref_id = $ref_id;
           """.query[AnltxNode]
    runQuery(query.to[Vector])
  }

  def getChartForGraph(graph_id:Int): DBResult[AnltxChart] = {
    val query =
      sql"""select c.chart_id, c.graph_id, c.name, c.description
            from anltx_charts c
            where c.graph_id = $graph_id;
           """.query[AnltxDBChart].unique
    val result = runQuery(query)
    result.map(c => new AnltxChart(c))
  }

//  def getLabelsForChart(chart_id: Int): DBResult[Vector[AnltxLabel]] = {
//    val query =
//      sql"""select
//            from anlytx_labels l
//            where l.label_id in (
//                select label_id
//                from anltx_edge_labels e
//                where e.chart_id = $chart_id
//            )
//            or l.label_id in (
//                select n.label_id
//                from anltx_node_labels n
//                where n.chart_id = $chart_id
//            )
//           """.query[AnltxLabel]
//    runQuery(query.to[Vector])
//  }

  def getNodeLabelsForChart(chart_id:Int): DBResult[Vector[AnltxNodeLabel]] = {
    val query =
      sql"""select n.node_id, n.label_id, n.expression, n.chart_id
    from anltx_node_labels n
    where n.chart_id = $chart_id
   """.query[AnltxNodeLabel]
    runQuery(query.to[Vector])
  }

  def getEdgeLabelsForChart(chart_id: Int): DBResult[Vector[AnltxEdgeLabel]] = {
    val query =
      sql"""select e.edge_id, e.label_id, e.chart_id
        from anltx_edge_labels e
        where e.chart_id = $chart_id
       """.query[AnltxEdgeLabel]
    runQuery(query.to[Vector])
  }

  def getLabelsForLabelIds(label_ids:Vector[Int]): DBResult[Vector[AnltxLabel]] = {
    val query =
      sql"""select l.label_id, l.label_type, l.ui_name, l.description, l.selected, l.properties
        from anltx_labels l
        where l.label_id = ANY(${label_ids})
       """.query[AnltxLabel]
    runQuery(query.to[Vector])
  }


}
