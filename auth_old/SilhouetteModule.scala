//package auth
//
//import com.google.inject.{AbstractModule, Provides}
//import com.mohiva.play.silhouette.api.util._
//import com.mohiva.play.silhouette.api.{Environment, EventBus, Silhouette, SilhouetteProvider}
//import com.mohiva.play.silhouette.impl.providers._
//import com.mohiva.play.silhouette.impl.util._
//import auth.data.{UserDAO, UserDAOImpl}
//import com.mohiva.play.silhouette.persistence.daos.{DelegableAuthInfoDAO, InMemoryAuthInfoDAO}
//import net.codingwell.scalaguice.ScalaModule
//
//import scala.concurrent.ExecutionContext.Implicits.global
//
///**
// * The Guice module which wires all Silhouette dependencies.
// */
//class SilhouetteModule extends AbstractModule with ScalaModule {
//
//  /**
//   * Configures the module.
//   */
//  override def configure() {
//    bind[Silhouette[DefaultEnv]].to[SilhouetteProvider[DefaultEnv]]
//    bind[UserService].to[UserServiceImpl]
//    bind[UserDAO].to[UserDAOImpl]
//    bind[CacheLayer].to[PlayCacheLayer]
//    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
//    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
//    bind[EventBus].toInstance(EventBus())
//    bind[Clock].toInstance(Clock())
//
//    bind[DelegableAuthInfoDAO[OAuth2Info]].toInstance(new InMemoryAuthInfoDAO[OAuth2Info])
//
//  }
//
//  /**
//   * Provides the Silhouette environment.
//   *
//   * @param eventBus The event bus instance.
//   * @return The Silhouette environment.
//   */
//  @Provides
//  def provideEnvironment(authService: GoogleAuthService, eventBus: EventBus): Environment[DefaultEnv] = {
//
//    Environment[DefaultEnv](
//      authService.user,
//      authService.cookieAuth,
//      Seq(),
//      eventBus
//    )
//  }
//}
