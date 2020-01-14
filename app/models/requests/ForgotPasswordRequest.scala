package models.requests

import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration}

case class ForgotPasswordRequest(
  email: String
) {
  require(
    email.nonEmpty && email.length <= 320,
    "email should not be empty and cannot have more than 320 characters."
  )
}

object ForgotPasswordRequest {
  implicit val fontCase = JsonConfiguration(SnakeCase)
  implicit val format = Json.format[ForgotPasswordRequest]
}
