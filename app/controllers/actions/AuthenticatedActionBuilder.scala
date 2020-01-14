package controllers.actions

import java.time.Clock

import javax.inject.Inject
import models.JwtInfo
import pdi.jwt.JwtSession._
import play.api.Configuration
import play.api.mvc.Results._
import play.api.mvc._
import repository.UserRepository

import scala.concurrent.{ExecutionContext, Future}

case class AuthenticatedRequest[A](jwtInfo: JwtInfo, request: Request[A]) extends WrappedRequest[A](request)

class AuthenticatedActionBuilder @Inject() (
  p: BodyParsers.Default,
  userRepository: UserRepository
)(
  implicit ec: ExecutionContext,
  conf: Configuration
) extends ActionBuilder[AuthenticatedRequest, AnyContent] {
  implicit val clock: Clock = Clock.systemUTC

  override def invokeBlock[A](
    request: Request[A],
    block: AuthenticatedRequest[A] => Future[Result]
  ): Future[Result] = {
    request.jwtSession.getAs[JwtInfo](JwtInfo.IDENTIFIER) match {
      case Some(jwtInfo) =>
        userRepository.getOptByEmail(jwtInfo.userEmail).flatMap {
          case Some(user) =>
            if (user.id == jwtInfo.userId)
              block(AuthenticatedRequest(jwtInfo, request))
                .map(_.refreshJwtSession(request))
            else
              Future.successful(Results.Forbidden("Unauthorized access."))
          case None =>
            Future.successful(Results.Forbidden("Unauthorized access."))
        }
      case _ =>
        Future(Unauthorized("You are not allowed to do this."))
    }
  }

  override def parser: BodyParser[AnyContent] = p

  override protected def executionContext: ExecutionContext = ec
}
