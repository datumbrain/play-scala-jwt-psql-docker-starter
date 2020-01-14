package models.requests

import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration}
import repository.rows.UserRow
import utility.CryptoUtility

case class SignUpUserRequest(
  firstName: String,
  lastName: String,
  email: String,
  password: String
) {
  require(
    firstName.nonEmpty && firstName.length <= 50,
    "first_name should not be empty and should have less than 51 characters."
  )
  require(
    lastName.nonEmpty && lastName.length <= 50,
    "last_name should not be empty and should have less than 51 characters."
  )
  require(
    email.nonEmpty && email.length <= 320,
    "email should not be empty and cannot have more than 320 characters."
  )
  require(
    password.nonEmpty && password.length >= 8 && password.length <= 64,
    "password should not be empty and greater than or equal to 8 characters at least or less than or equal to 64 characters at most."
  )
}

object SignUpUserRequest {
  implicit val fontCase = JsonConfiguration(SnakeCase)
  implicit val format = Json.format[SignUpUserRequest]

  def toUserRow(createUserRequest: SignUpUserRequest): UserRow = {
    UserRow(
      id = 0L,
      firstName = createUserRequest.firstName,
      lastName = createUserRequest.lastName,
      email = createUserRequest.email,
      passwordHash = CryptoUtility.createPasswordHash(createUserRequest.password)
    )
  }
}
