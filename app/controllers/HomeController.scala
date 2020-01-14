package controllers

import javax.inject._
import models.responses.LandingPageResponse
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject() (
  cc: ControllerComponents
)(
  implicit assetsFinder: AssetsFinder,
  executionContext: ExecutionContext
) extends AbstractController(cc) {
  def index: Action[AnyContent] = Action {
    Ok(Json.toJson(LandingPageResponse()))
  }
}
