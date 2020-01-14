package controllers

import implicits.ControllerImplicits
import javax.inject._
import models.requests.{ForgotPasswordRequest, ResetPasswordRequest}
import play.api.mvc._
import repository.rows.UserInviteRow
import repository.{UserInviteRepository, UserRepository}
import services.EmailService
import utility.CryptoUtility

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ForgotPasswordController @Inject() (
  cc: ControllerComponents,
  userRepository: UserRepository,
  userInviteRepository: UserInviteRepository,
  emailService: EmailService
)(
  implicit assetsFinder: AssetsFinder,
  executionContext: ExecutionContext
) extends AbstractController(cc)
    with ControllerImplicits {
  def forgotPassword: Action[AnyContent] = Action.async { implicit r =>
    postRequest[ForgotPasswordRequest] { body =>
      userRepository.getOptByEmail(body.email).flatMap {
        case Some(user) =>
          userInviteRepository
            .create(UserInviteRow.resetPasswordInvite(user.id))
            .flatMap { token =>
              emailService.sendResetPasswordEmail(token, user.email)
            }
            .map(_ => Ok("Forgot password email sent, check mail."))
        case None =>
          Future.successful(NotFound)
      }
    }
  }

  def resetPassword(token: String): Action[AnyContent] = Action.async {
    implicit request =>
      postRequest[ResetPasswordRequest] { r =>
        userInviteRepository.getOptByToken(token).flatMap {
          case Some(userInviteRow) =>
            if (userInviteRow.isExpired) {
              userInviteRepository
                .deprecate(userInviteRow)
                .map(_ => BadRequest("The token was expired."))
            } else {
              resetPasswordHelper(
                userInviteRow.userId,
                r.password
              ).flatMap(_ =>
                  userInviteRepository
                    .deprecate(userInviteRow)
                )
                .map(_ => Ok("Password reset successful."))
            }
          case None => Future.successful(BadRequest)
        }
      }
  }

  private def resetPasswordHelper(
    userId: Long,
    newPassword: String
  ): Future[Boolean] = {
    for {
      _ <- userRepository
        .updateEmailVerification(
          userId,
          status = true
        )
      _ <- userRepository
        .updatePasswordHashByUserId(
          userId,
          CryptoUtility
            .createPasswordHash(newPassword)
        )
    } yield true
  }
}
