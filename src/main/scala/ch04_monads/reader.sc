/** Exercise: login system */
case class Db(
    usernames: Map[Int, String],
    passwords: Map[String, String]
)

import cats.data.Reader
import cats.Applicative
import cats.syntax.applicative._ // for pure

type DbReader[A] = Reader[Db, A]
// define implicit to prevent ambiguity error
implicit val dbReaderApplicative: Applicative[DbReader] = Applicative.catsApplicativeForArrow


def findUsername(id: Int): DbReader[Option[String]] =
Reader(db => db.usernames.get(id))

def checkPassword(username: String, inputPassword: String): DbReader[Boolean] =
Reader(db => db.passwords.get(username).contains(inputPassword))


def checkLogin(userId: Int, inputPassword: String): DbReader[Boolean] =
  for {
      username <- findUsername(userId)
      passwordOk <- username
        .map(u => checkPassword(u, inputPassword))
        .getOrElse(false.pure[DbReader])
  } yield passwordOk

/** Example test code */
val users = Map(
  1 -> "dade",
  2 -> "kate",
  3 -> "margo"
)
val passwords = Map(
  "dade" -> "zerocool",
  "kate" -> "acidburn",
  "margo" -> "secret"
)
val db = Db(users, passwords)

assert(checkLogin(1, "zerocool").run(db))

assert(!checkLogin(4, "davinci").run(db))
