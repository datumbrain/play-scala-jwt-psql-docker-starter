package filters

import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class ExampleFilter @Inject()(implicit ec: ExecutionContext) extends EssentialFilter {
  override def apply(next: EssentialAction) = EssentialAction { request =>
    next(request)
  }
}
