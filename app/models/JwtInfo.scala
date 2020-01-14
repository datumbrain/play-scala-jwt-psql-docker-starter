package models

import models.UserRoleEnum.UserRole
import play.api.libs.json.Json

case class JwtInfo(userId: Long, userEmail: String, userRole: UserRole)

object JwtInfo {
  val IDENTIFIER = "jwt-info"

  implicit val format = Json.format[JwtInfo]
}