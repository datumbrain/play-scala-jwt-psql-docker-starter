package controllers

import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(
  cc: ControllerComponents
)(
  implicit assetsFinder: AssetsFinder,
  executionContext: ExecutionContext
) extends AbstractController(cc) {


}
