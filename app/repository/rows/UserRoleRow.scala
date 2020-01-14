package repository.rows

import java.sql.Timestamp

import implicits.TimestampImplicits._
import models.UserRoleEnum
import utility.DateUtility

case class UserRoleRow(
  id: Long,
  role: String,
  creationTs: Timestamp = DateUtility.now(),
  updationTs: Option[Timestamp] = None
)

object UserRoleRow {
  lazy val DEFAULT_ID = 1
  lazy val DEFAULT_ROLE = UserRoleEnum.USER
}
