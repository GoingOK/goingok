package auth

import com.mohiva.play.silhouette.api.EventBus
import com.mohiva.play.silhouette.api.crypto.CrypterAuthenticatorEncoder
import com.mohiva.play.silhouette.api.util.{Clock, FingerprintGenerator, IDGenerator, PlayHTTPLayer}
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings, JcaSigner, JcaSignerSettings}
import com.mohiva.play.silhouette.impl.authenticators.{CookieAuthenticatorService, CookieAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.providers.oauth2.GoogleProvider
import com.mohiva.play.silhouette.impl.providers.state.{CsrfStateItemHandler, CsrfStateSettings}
import com.mohiva.play.silhouette.impl.providers.{DefaultSocialStateHandler, OAuth2Settings, SocialProviderRegistry}
import javax.inject.Inject
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.CookieHeaderEncoding

import scala.concurrent.ExecutionContext

class GoogleAuthService @Inject()(userService: UserService, eventBus: EventBus, cookieHeaderEncoding: CookieHeaderEncoding,
                                  fingerprintGenerator: FingerprintGenerator, idGenerator: IDGenerator,
                                  client: WSClient, configuration: Configuration, clock: Clock)
                                 (implicit ex: ExecutionContext) {

  //def environment = Environment[DefaultEnv](userService, cookieAuth, Seq(), eventBus)

  def cookieAuth = {
    val authConfig = configuration.underlying.as[CookieAuthenticatorSettings]("silhouette.authenticator")
    val crypterConfig = configuration.underlying.as[JcaCrypterSettings]("silhouette.authenticator.crypter")
    val signerConfig = configuration.underlying.as[JcaSignerSettings]("silhouette.authenticator.signer")
    val signer = new JcaSigner(signerConfig)
    val crypter = new JcaCrypter(crypterConfig)
    val authenticatorEncoder = new CrypterAuthenticatorEncoder(crypter)

    new CookieAuthenticatorService(authConfig, None, signer, cookieHeaderEncoding, authenticatorEncoder, fingerprintGenerator, idGenerator, clock)
  }

  def user = userService

  private lazy val httpLayer = new PlayHTTPLayer(client)
  private lazy val csrfStateItemConfig = configuration.underlying.as[JcaSignerSettings]("silhouette.csrfStateItemHandler.signer")
  private lazy val csrfStateItemSigner = new JcaSigner(csrfStateItemConfig)
  private lazy val csrfStateConfig = configuration.underlying.as[CsrfStateSettings]("silhouette.csrfStateItemHandler")
  private lazy val csrfStateItemHandler = new CsrfStateItemHandler(csrfStateConfig, idGenerator, csrfStateItemSigner)
  private lazy val socialSignerConfig = configuration.underlying.as[JcaSignerSettings]("silhouette.socialStateHandler.signer")
  private lazy val socialSigner = new JcaSigner(socialSignerConfig)
  private lazy val socialStateHandler = new DefaultSocialStateHandler(Set(csrfStateItemHandler), socialSigner)
  lazy val googleProvider = new GoogleProvider(httpLayer, socialStateHandler, configuration.underlying.as[OAuth2Settings]("silhouette.google"))

  def registry = SocialProviderRegistry(Seq(googleProvider))

}
