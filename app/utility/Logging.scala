package utility

import play.api.Logger

trait Logging {
  private val logger = Logger("mplo-logger")

  def logInfo(message: String): Unit = logger.info(message)

  def logDebug(message: String): Unit = logger.debug(message)

  def logWarn(message: String): Unit = logger.warn(message)

  def logError(message: String): Unit = logger.error(message)
}
