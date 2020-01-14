package repository

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import repository.rows.UserRoleRow
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import utility.DateUtility

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRoleRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(
  implicit ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  private val table = TableQuery[UserRoleTable]

  def getOptById(id: Long): Future[Option[UserRoleRow]] = db.run {
    table.filter(_.id === id).result.headOption
  }

  def getById(id: Long): Future[UserRoleRow] = db.run {
    table.filter(_.id === id).result.head
  }

  private class UserRoleTable(tag: Tag)
      extends Table[UserRoleRow](tag, "user_role") {
    def * : ProvenShape[UserRoleRow] =
      (
        id,
        role,
        creationTs,
        updationTs
      ) <> ((UserRoleRow.apply _).tupled, UserRoleRow.unapply)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def role: Rep[String] = column[String]("role", O.Length(32))

    def creationTs: Rep[Timestamp] =
      column[Timestamp]("creation_ts", O.Default(DateUtility.now()))

    def updationTs: Rep[Option[Timestamp]] =
      column[Option[Timestamp]]("updation_ts")
  }
}
