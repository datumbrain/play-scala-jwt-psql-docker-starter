package models.requests

import play.api.libs.json.Json

case class UserSignInRequest(email: String, password: String)

object UserSignInRequest {
  implicit val format = Json.format[UserSignInRequest]
}
