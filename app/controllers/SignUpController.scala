package controllers

import implicits.ControllerImplicits
import javax.inject._
import models.requests.SignUpUserRequest
import org.postgresql.util.PSQLException
import play.api.libs.json.Json
import play.api.mvc._
import repository.rows.UserInviteRow
import repository.{UserInviteRepository, UserRepository}
import services.EmailService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SignUpController @Inject() (
  cc: ControllerComponents,
  userRepository: UserRepository,
  userInviteRepository: UserInviteRepository,
  emailService: EmailService
)(
  implicit assetsFinder: AssetsFinder,
  executionContext: ExecutionContext
) extends AbstractController(cc)
    with ControllerImplicits {
  def signUp: Action[AnyContent] = Action.async { implicit request =>
    postRequest[SignUpUserRequest] { body =>
      userRepository.getOptByEmail(body.email).flatMap {
        case Some(_) => Future.successful(Conflict("Already present."))
        case None =>
          userRepository
            .create(SignUpUserRequest.toUserRow(body))
            .flatMap { userId =>
              for {
                token <- userInviteRepository
                  .create(UserInviteRow.newUserInvite(userId))
                _ <- emailService
                  .sendRegistrationEmail(token, body.email)
              } yield Ok(Json.toJson("User created."))
            }
            .recover {
              case _: PSQLException =>
                Conflict("May be a conflict has occurred.")
              case e: IllegalArgumentException =>
                BadRequest(e.getMessage)
            }
      }
    }
  }

  def emailVerification(token: String): Action[AnyContent] = Action.async {
    implicit request =>
      userInviteRepository.getOptByToken(token).flatMap {
        case Some(userInviteRow) =>
          if (userInviteRow.isExpired) {
            for {
              _ <- userRepository
                .updateEmailVerification(userInviteRow.userId, status = false)
              _ <- userInviteRepository.deprecate(userInviteRow)
            } yield BadRequest("The token was expired.")
          } else {
            for {
              _ <- userRepository
                .updateEmailVerification(userInviteRow.userId, status = true)
              _ <- userInviteRepository.deprecate(userInviteRow)
            } yield Ok("Email verified.")
          }
        case None => Future.successful(NotFound)
      }
  }
}
