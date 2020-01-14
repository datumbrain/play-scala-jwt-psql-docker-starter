package utility

import java.security.MessageDigest

import org.mindrot.jbcrypt.BCrypt

import scala.util.Try

object CryptoUtility {
  def createHash(str: String): String = {
    MessageDigest
      .getInstance("MD5")
      .digest(str.getBytes)
      .map(0xFF & _)
      .map {
        "%02x".format(_)
      }
      .foldLeft("") {
        _ + _
      }
  }

  def createRandomHash: String = {
    createHash(System.currentTimeMillis.toString)
  }

  def createPasswordHash(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  def matchPasswordHash(password: String, hashedPassword: String): Boolean = {
    Try {
      BCrypt.checkpw(password, hashedPassword)
    }.getOrElse(false)
  }
}
