package utility

import com.typesafe.config.ConfigFactory

object Configuration extends Logging {
  private val config = ConfigFactory.load()

  val HTTP_PORT: Long = config.getLong("myproj.http.port")

  val EMAIL_VERIFICATION_TOKEN_EXPIRY: Long =
    config.getLong("myproj.tokens.email.verification.expiry") * 60 * 1000
}
