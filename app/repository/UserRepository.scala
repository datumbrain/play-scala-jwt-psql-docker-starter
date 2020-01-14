package repository

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import repository.rows.{UserRoleRow, UserRow}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape
import utility.DateUtility

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(
  implicit ec: ExecutionContext
) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._
  private val table = TableQuery[UserTable]

  /**
    * List all the users
    * @return all users
    */
  def listAll: Future[Seq[UserRow]] = {
    db.run {
      table.result
    }
  }

  /**
    * Insert user into database
    * @param userRow the user row
    * @return id of newly created row
    */
  def create(userRow: UserRow): Future[Long] = {
    db.run {
      table.returning(table.map(_.id)) += userRow
    }
  }

  /**
    * Update email verification boolean by id, status
    * @param id id of user
    * @param status can be true, false
    * @return how many rows updated (always one because id is unique)
    */
  def updateEmailVerification(id: Long, status: Boolean): Future[Int] = db.run {
    table.filter(_.id === id).map(_.isEmailVerified).update(status)
  }

  /**
    * Get user option by email
    * @param email user email
    * @return user row found
    */
  def getOptByEmail(email: String): Future[Option[UserRow]] = db.run {
    table.filter(_.email === email).result.headOption
  }

  /**
    * Update password hash by user ID
    * @param userId user's ID
    * @param newHash replace passwordHash with this value
    * @return how many rows were updated
    */
  def updatePasswordHashByUserId(userId: Long, newHash: String): Future[Int] = {
    db.run(table.filter(_.id === userId).map(_.passwordHash).update(newHash))
  }

  private class UserTable(tag: Tag) extends Table[UserRow](tag, "user") {
    def * : ProvenShape[UserRow] =
      (
        id,
        firstName,
        lastName,
        email,
        passwordHash,
        addressLine1,
        addressLine2,
        pictureUrl,
        isActive,
        roleId,
        creationTs,
        updationTs,
        isEmailVerified
      ) <> ((UserRow.apply _).tupled, UserRow.unapply)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def firstName: Rep[String] = column[String]("first_name", O.Length(50))

    def lastName: Rep[String] = column[String]("last_name", O.Length(50))

    def email: Rep[String] = column[String]("email", O.Unique, O.Length(320))

    def passwordHash: Rep[String] =
      column[String]("password_hash", O.Length(64))

    def addressLine1: Rep[Option[String]] = column[Option[String]]("address_line_1")

    def addressLine2: Rep[Option[String]] = column[Option[String]]("address_line_2")

    def pictureUrl: Rep[Option[String]] = column[Option[String]]("picture_url")

    def isActive: Rep[Boolean] = column[Boolean]("is_active")

    def roleId: Rep[Long] = column[Long]("role_id", O.Default(UserRoleRow.DEFAULT_ID))

    def creationTs: Rep[Timestamp] =
      column[Timestamp]("creation_ts", O.Default(DateUtility.now()))

    def updationTs: Rep[Option[Timestamp]] =
      column[Option[Timestamp]]("updation_ts")

    def isEmailVerified: Rep[Boolean] = column[Boolean]("is_email_verified")
  }
}
