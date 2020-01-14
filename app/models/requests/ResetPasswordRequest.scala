package models.requests

import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration}

case class ResetPasswordRequest(
  password: String
) {
  require(
    password.nonEmpty && password.length >= 8 && password.length <= 64,
    "password should not be empty and greater than or equal to 8 characters at least or less than or equal to 64 characters at most."
  )
}

object ResetPasswordRequest {
  implicit val fontCase = JsonConfiguration(SnakeCase)
  implicit val format = Json.format[ResetPasswordRequest]
}
