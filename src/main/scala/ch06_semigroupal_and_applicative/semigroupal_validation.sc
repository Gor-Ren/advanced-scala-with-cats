// the Either Semigroupal had fail-fast semantics because that's
// how its Monad (and flatMap) works.

// Cats provides a Validated type which has a Semigroupal but no
// Monad, and implements accumulative behaviour:

import cats.Semigroupal
import cats.data.Validated
import cats.instances.list._ // for Monoid

type ErrorsOr[A] = Validated[List[String], A]

Semigroupal[ErrorsOr].product(
  Validated.invalid(List("error one")),
  Validated.invalid(List("error two"))
)

// we can also use some syntax methods:
import cats.syntax.validated._
"success result".valid[List[String]]

// we can express the fact the list will be non-empty and avoid
// specifying a `List` by using the `Nel` methods:
"success result".validNel[String] // NonEmptyList[String]
(new IllegalArgumentException).invalid[Long]

// we can use the Applicative/ApplicativeError syntaxes, too
import cats.syntax.applicative._
import cats.syntax.applicativeError._
List("empty stack!").raiseError[ErrorsOr, Int]

// finally, there are some helper and conversion methods on Validated;
Validated.catchOnly[NumberFormatException] { "bad".toInt }
Validated.catchOnly[NumberFormatException] { "3".toInt }

/** Accumulating errors */
// we can accumulate errors using a Semigroup
// we must import a Semigroup instance for the error type
import cats.instances.list._  // for Semigroupal
import cats.data.ValidatedNel
import cats.Invariant

// we have to define a type with a single type param
type Result[E] = ValidatedNel[Throwable, E]

val nelSemigroupal: Semigroupal[Result] = implicitly
val nelInvariant: Invariant[Result] = implicitly

val failableOp1: Result[Int] =
  new NoSuchElementException("nope").invalidNel[Int]
val failableOp2: Result[Int] =
  new RuntimeException("database failure").invalidNel[Int]
val failableOp3: Result[Int] =
  42.validNel

import cats.syntax.apply._  // for tupled
(failableOp1, failableOp2, failableOp3).tupled

/** 6.4.4. Exercise: Form Validation */
case class User(name: String, age: Int)

import cats.data.NonEmptyList
type FormData = Map[String, String]
type FailFast[A] = Either[NonEmptyList[String], A]
type FailSlow[A] = ValidatedNel[String, A]

def getValue(field: String, data: FormData): FailFast[String] =
  data.get(field)
    .toRight(NonEmptyList.of(s"No such field: $field"))

import cats.syntax.either._

def parseInt(value: String): FailFast[Int] =
  Either.catchOnly[NumberFormatException] {
    value.toInt
  }.leftMap(_ => NonEmptyList.of(s"Illegal integer value: $value"))

def nonBlank(str: String): FailFast[String] =
  Right(str)
    .ensure(NonEmptyList.of(s"Name cannot be blank"))(_.nonEmpty)

def nonNegative(i: Int): FailFast[Int] =
  Right(i)
    .ensure(NonEmptyList.of(s"Age must be non-negative"))(_ >= 0)

def readName(data: FormData): FailSlow[String] =
  Validated.fromEither(
    for {
      name <- getValue("name", data)
      _ <- nonBlank(name)
    } yield name
  )

def readAge(data: FormData): FailSlow[Int] =
  Validated.fromEither(
    for {
      ageString <- getValue("age", data)
      age <- parseInt(ageString)
      _ <- nonNegative(age)
    } yield age
  )

def readUser(data: FormData): FailSlow[User] =
  (
    readName(data),
    readAge(data)
  ).mapN(User)


/* Example usage */
val goodData: FormData = Map("name" -> "Gordon", "age" -> "99")
val noNameData: FormData = Map("age" -> "99")
val bothBadData: FormData = Map("name" -> "", "age" -> "qwerty")

readUser(goodData)
readUser(noNameData)
readUser(bothBadData)