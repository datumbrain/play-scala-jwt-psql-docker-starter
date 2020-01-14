package controllers.actions

import javax.inject.Inject
import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc.{
  AbstractController,
  ControllerComponents,
  DefaultActionBuilder,
  PlayBodyParsers
}

case class SecuredControllerComponents @Inject()(
  adminActionBuilder: AdminActionBuilder,
  authenticatedActionBuilder: AuthenticatedActionBuilder,
  actionBuilder: DefaultActionBuilder,
  parsers: PlayBodyParsers,
  messagesApi: MessagesApi,
  langs: Langs,
  fileMimeTypes: FileMimeTypes,
  executionContext: scala.concurrent.ExecutionContext
) extends ControllerComponents

class SecuredController @Inject()(scc: SecuredControllerComponents)
    extends AbstractController(scc) {

  def AdminAction: AdminActionBuilder = scc.adminActionBuilder

  def AuthenticatedAction: AuthenticatedActionBuilder =
    scc.authenticatedActionBuilder
}
