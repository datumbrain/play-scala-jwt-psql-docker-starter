package models.responses.admin

import java.sql.Timestamp

import implicits.TimestampImplicits._
import models.responses.HttpReponse
import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration}
import repository.rows.UserRow
import utility.DateUtility

case class ListUsersResponse(
  id: Long,
  firstName: String,
  lastName: String,
  email: String,
  addressLine1: Option[String] = None,
  addressLine2: Option[String] = None,
  isActive: Boolean = false,
  creationTs: Timestamp = DateUtility.now(),
  updationTs: Option[Timestamp] = None,
  isEmailVerified: Boolean = false
) extends HttpReponse

object ListUsersResponse {
  implicit val fontCase = JsonConfiguration(SnakeCase)
  implicit val format = Json.format[ListUsersResponse]

  def fromUserRow(userRow: UserRow): ListUsersResponse = {
    ListUsersResponse(
      id = userRow.id,
      firstName = userRow.firstName,
      lastName = userRow.lastName,
      email = userRow.email,
      addressLine1 = userRow.addressLine1,
      addressLine2 = userRow.addressLine2,
      isActive = userRow.isActive,
      creationTs = userRow.creationTs,
      updationTs = userRow.updationTs,
      isEmailVerified = userRow.isEmailVerified
    )
  }
}
