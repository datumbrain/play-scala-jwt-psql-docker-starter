package models

import play.api.libs.json.Json

object UserRoleEnum extends Enumeration {
  type UserRole = Value
  val ADMIN, USER = Value

  implicit val format = Json.formatEnum(this)
}
