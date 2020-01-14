package controllers

import controllers.actions.{SecuredController, SecuredControllerComponents}
import javax.inject._
import models.responses.admin.ListUsersResponse
import play.api.libs.json.Json
import play.api.mvc._
import repository.UserRepository

import scala.concurrent.ExecutionContext

@Singleton
class AdminController @Inject() (
  scc: SecuredControllerComponents,
  userRepository: UserRepository
)(
  implicit assetsFinder: AssetsFinder,
  executionContext: ExecutionContext
) extends SecuredController(scc) {
  def listAll: Action[AnyContent] = AdminAction.async { implicit request =>
    userRepository.listAll.map(users =>
      Ok(Json.toJson(users.map(ListUsersResponse.fromUserRow)))
    )
  }
}
