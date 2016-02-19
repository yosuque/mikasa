package mikasa.logger

import org.slf4j.LoggerFactory

private[mikasa] trait MikasaLogger {

  private val LOG = LoggerFactory.getLogger("mikasa")
  private val ERROR = LoggerFactory.getLogger("error")

  def info(message: String) = LOG.info(message)

  def warn(message: String) = LOG.warn(message)

  def error(message: String) = LOG.error(message)

  def error(message: String, t: Throwable) = {
    LOG.error(s"${message} see error log.")
    ERROR.error(message, t)
  }
}

object MikasaLogger extends MikasaLogger
