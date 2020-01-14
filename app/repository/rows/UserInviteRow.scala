package repository.rows

import java.sql.Timestamp

import implicits.TimestampImplicits._
import utility.{Configuration, CryptoUtility, DateUtility}

case class UserInviteRow(
  id: Long = 0L,
  userId: Long,
  token: String,
  isActive: Boolean,
  inviteType: String = "NEW",
  creationTs: Timestamp = DateUtility.now()
) {
  def isExpired: Boolean = {
    !isActive || {
      DateUtility
        .now()
        .getTime - creationTs.getTime > Configuration.EMAIL_VERIFICATION_TOKEN_EXPIRY
    }
  }
}

object UserInviteRow {
  val DEFAULT_INVITE_TYPE = "NEW"
  val RESET_PASSWORD_INVITE_TYPE = "RESET"

  private val INVITE_TYPES =
    Seq(DEFAULT_INVITE_TYPE, RESET_PASSWORD_INVITE_TYPE)

  def isValidInviteType(inviteType: String) =
    INVITE_TYPES.contains(inviteType)

  def newUserInvite(userId: Long): UserInviteRow = {
    UserInviteRow(
      userId = userId,
      token = CryptoUtility.createRandomHash,
      isActive = true,
      inviteType = DEFAULT_INVITE_TYPE
    )
  }

  def resetPasswordInvite(userId: Long): UserInviteRow = {
    UserInviteRow(
      userId = userId,
      token = CryptoUtility.createRandomHash,
      isActive = true,
      inviteType = RESET_PASSWORD_INVITE_TYPE
    )
  }
}
