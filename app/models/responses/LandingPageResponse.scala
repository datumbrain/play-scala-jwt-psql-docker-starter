package models.responses

import play.api.libs.json.Json

case class LandingPageResponse(
  message: String = LandingPageResponse.DEFAULT_MESSAGE
) extends HttpReponse

object LandingPageResponse {
  implicit val format = Json.format[LandingPageResponse]

  val DEFAULT_MESSAGE = "API is ready! Everything seems to be in order."
}
