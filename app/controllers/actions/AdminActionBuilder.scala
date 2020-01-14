package controllers.actions

import java.time.Clock

import javax.inject.Inject
import models.{JwtInfo, UserRoleEnum}
import pdi.jwt.JwtSession._
import play.api.Configuration
import play.api.mvc.Results.{Forbidden, Unauthorized}
import play.api.mvc.{ActionBuilderImpl, BodyParsers, Request, Result}

import scala.concurrent.{ExecutionContext, Future}

class AdminActionBuilder @Inject()(parser: BodyParsers.Default)(
  implicit ec: ExecutionContext,
  conf: Configuration
) extends ActionBuilderImpl(parser) {
  implicit val clock: Clock = Clock.systemUTC

  override def invokeBlock[A](
    request: Request[A],
    block: Request[A] => Future[Result]
  ): Future[Result] = {
    request.jwtSession.getAs[JwtInfo](JwtInfo.IDENTIFIER) match {
      case Some(jwtInfo: JwtInfo) if jwtInfo.userRole == UserRoleEnum.ADMIN =>
        block(AuthenticatedRequest(jwtInfo, request))
          .map(_.refreshJwtSession(request))
      case Some(_) =>
        Future(Forbidden.refreshJwtSession(request))
      case _ =>
        Future(Unauthorized)
    }
  }
}
