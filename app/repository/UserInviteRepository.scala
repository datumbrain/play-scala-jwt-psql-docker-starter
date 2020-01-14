package repository

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import repository.rows.UserInviteRow
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import utility.{DateUtility, Logging}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserInviteRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(
  implicit ec: ExecutionContext
) extends Logging {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  private val table = TableQuery[UserInviteTable]

  /**
    * Insert user invite into database
    * @param row the user invite row
    * @return id of newly created row
    */
  def create(row: UserInviteRow): Future[String] = {
    deprecateAllOlder(row).flatMap { _ =>
      db.run(table.returning(table.map(_.token)) += row)
    }
  }

  def getOptById(id: Long): Future[Option[UserInviteRow]] = {
    db.run {
      table.filter(_.id === id).result.headOption
    }
  }

  def getOptByToken(token: String): Future[Option[UserInviteRow]] = {
    db.run {
      table.filter(_.token === token).result.headOption
    }
  }

  private def deprecateAllOlder(row: UserInviteRow): Future[Int] = {
    db.run(
        table
          .filter(
            r => r.userId === row.userId && r.inviteType === row.inviteType
          )
          .map(_.isActive)
          .update(false)
      )
      .recover {
        case e: Throwable =>
          logError(e.getMessage)
          0
      }
  }

  def deprecate(row: UserInviteRow): Future[Int] = {
    db.run {
      table
        .filter(_.id === row.id)
        .map(_.isActive)
        .update(false)
    }
  }

  private class UserInviteTable(tag: Tag)
      extends Table[UserInviteRow](tag, "user_invite") {
    def * : ProvenShape[UserInviteRow] =
      (
        id,
        userId,
        token,
        isActive,
        inviteType,
        creationTs
      ) <> ((UserInviteRow.apply _).tupled, UserInviteRow.unapply)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userId: Rep[Long] = column[Long]("user_id")

    def token: Rep[String] = column[String]("token", O.Length(32))

    def isActive: Rep[Boolean] = column[Boolean]("is_active")

    def inviteType: Rep[String] = column[String]("invite_type", O.Length(6))

    def creationTs: Rep[Timestamp] =
      column[Timestamp]("creation_ts", O.Default(DateUtility.now()))
  }
}
