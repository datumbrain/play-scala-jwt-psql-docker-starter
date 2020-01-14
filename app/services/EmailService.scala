package services

import javax.inject.{Inject, Singleton}
import play.api.Environment
import play.api.libs.mailer.{Email, MailerClient}
import play.twirl.api.Html
import utility.Configuration

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EmailService @Inject() (
  mailerClient: MailerClient,
  environment: Environment
)(implicit ec: ExecutionContext) {
  def sendRegistrationEmail(
    token: String,
    userEmail: String
  ): Future[String] = {
    Future {
      val emailHtml =
        Html(
          s"""Sign up by clicking <a href='http://localhost:${Configuration.HTTP_PORT}/v1/user/$token/verify'>here</a>.<br /><br />This link will get expired in 12 hours."""
        )

      val email = Email(
        subject = s"Complete your MyProject Registration",
        from = "your@email.com",
        to = Seq(userEmail),
        bodyHtml = Some(emailHtml.body)
      )

      mailerClient.send(email)
    }
  }

  def sendResetPasswordEmail(
    token: String,
    userEmail: String
  ): Future[String] = {
    Future {
      val emailHtml =
        Html(
          s"""Reset password by clicking <a href='http://localhost:${Configuration.HTTP_PORT}/v1/user/$token/reset'>here</a>.<br /><br />This link will get expired in 12 hours."""
        )

      val email = Email(
        subject = s"Reset Your MyProject Password",
        from = "your@email.com",
        to = Seq(userEmail),
        bodyHtml = Some(emailHtml.body)
      )

      mailerClient.send(email)
    }
  }
}
