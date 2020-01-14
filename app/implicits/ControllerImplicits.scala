package implicits

import play.api.libs.json.{JsValue, Json, Reads}
import play.api.mvc.{AnyContent, Request, Result, Results}

import scala.concurrent.Future

trait ControllerImplicits {
  protected def postRequest[A](
    fn: (A) => Future[Result]
  )(
    implicit reads: Reads[A],
    request: Request[AnyContent]
  ): Future[Result] = {
    request.body.asJson
      .map { json =>
        Json.fromJson[A](json).asOpt match {
          case Some(r) => fn(r)
          case None =>
            Future.successful(Results.BadRequest("Bad request."))
        }
      }
      .getOrElse(
        Future.successful(Results.BadRequest("Request body not found."))
      )
  }
}
