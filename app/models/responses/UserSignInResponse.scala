package models.responses

import play.api.libs.json.Json

case class UserSignInResponse(userName: String)

object UserSignInResponse {
  implicit val format = Json.format[UserSignInResponse]
}
