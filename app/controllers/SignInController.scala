package controllers

import java.time.Clock

import implicits.ControllerImplicits
import javax.inject._
import models.requests.UserSignInRequest
import models.responses.UserSignInResponse
import models.{JwtInfo, UserRoleEnum}
import pdi.jwt.JwtSession._
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc._
import repository.{UserRepository, UserRoleRepository}
import utility.CryptoUtility

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SignInController @Inject() (
  cc: ControllerComponents,
  userRepository: UserRepository,
  userRoleRepository: UserRoleRepository
)(
  implicit assetsFinder: AssetsFinder,
  executionContext: ExecutionContext,
  configuration: Configuration
) extends AbstractController(cc)
    with ControllerImplicits {
  implicit val clock: Clock = Clock.systemUTC

  def signIn: Action[AnyContent] = Action.async { implicit request =>
    postRequest[UserSignInRequest] { body =>
      userRepository.getOptByEmail(body.email).flatMap {
        case Some(user) =>
          userRoleRepository.getById(user.roleId).flatMap { roleRow =>
            val validSignIn = CryptoUtility
              .matchPasswordHash(
                body.password,
                user.passwordHash
              )
            val isValidated = user.isEmailVerified
            if (validSignIn && isValidated) {
              Future.successful(
                Ok(
                  Json.toJson(
                    UserSignInResponse(userName = user.firstName)
                  )
                ).addingToJwtSession(
                  JwtInfo.IDENTIFIER,
                  JwtInfo(
                    user.id,
                    user.email,
                    UserRoleEnum.withName(roleRow.role)
                  )
                )
              )
            } else if (!isValidated && validSignIn) {
              Future
                .successful(NotFound("Please validate your email first."))
            } else {
              Future
                .successful(NotFound("Email or password are incorrect."))
            }
          }
        case None => Future.successful(NotFound("Email doesn't exist."))
      }
    }
  }
}
