import com.google.inject.AbstractModule
import javax.inject.{Inject, Singleton}
import utility.Logging

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[SystemGlobal]).asEagerSingleton()
  }
}

@Singleton
class SystemGlobal @Inject() (
  ) extends Logging {
  def initialize(): Unit = {
    logInfo("System global initializing...")
  }

  initialize()
}
