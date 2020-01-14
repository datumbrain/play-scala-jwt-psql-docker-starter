package repository.rows

import java.sql.Timestamp

import implicits.TimestampImplicits._
import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration}
import utility.DateUtility

case class UserRow(
  id: Long,
  firstName: String,
  lastName: String,
  email: String,
  passwordHash: String,
  addressLine1: Option[String] = None,
  addressLine2: Option[String] = None,
  pictureUrl: Option[String] = None,
  isActive: Boolean = false,
  roleId: Long = UserRoleRow.DEFAULT_ID,
  creationTs: Timestamp = DateUtility.now(),
  updationTs: Option[Timestamp] = None,
  isEmailVerified: Boolean = false
)

object UserRow {
  implicit val fontCase = JsonConfiguration(SnakeCase)
  implicit val format = Json.format[UserRow]
}
