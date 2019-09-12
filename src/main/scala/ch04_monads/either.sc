// some practice with the Either monad

// it is right-biased: success results go in Right, errors in Left
// errors short-circuit

val either1: Either[String, Int] = Right(10)
val either2: Either[String, Int] = Right(32)

for {
  x <- either1
  y <- either2
} yield x + y

val eitherFail: Either[String, Int] = Left("failure")

for {
  x <- eitherFail
  y <- either1
} yield x + y

// cats syntax provides conversion methods:
import cats.syntax.either._ // for asLeft/Right

val a = 3.asRight  // smart constructor; has type Either, not Right
// smart constructors help type inference by avoiding over-narrowing the type

/** Divide x by all values in divs, returning result, or error if div 0 */
def divideBy(x: Float, divs: List[Float]): Either[String, Float] =
  divs.foldLeft(Either.right[String, Float](x))((result, div) =>
    result.flatMap(x =>
      if (div == 0)
        Left("divide by zero!")
      else
        Right(x / div)
    )
  )

assert(divideBy(2.0f, List.empty) == 2.0f.asRight)
assert(divideBy(2.0f, List(1.0f)) == 2.0f.asRight)
assert(divideBy(2.0f, List(1.0f, 2.0f)) == 1.0f.asRight)
assert(divideBy(2.0f, List(0.0f)).isLeft)

// cats adds useful methods
import cats.syntax.either._

"maths error!".asLeft[Int].orElse(Right(0))
"maths error!".asLeft[Int].getOrElse(0)

// provide a partial function to maybe recover
"maths error!".asLeft[Int].recover {
  case str: String => 0
}
// there's also recoverWith that returns an Either

1.asRight[String].ensure("1 not permitted!")(_ != 1)

// we can map on the Right, the Left, or both
1.asRight[String].map(_ + 1)
1.asRight[String].leftMap(_ + "hello") // does nothing on a Right
1.asRight[String].bimap(_ + "hello", _ + 2)

/* Error Handling */

// common error handling patterns are to use `Throwable` as the error type,
// or an algebraic data type
val throwableOp: Either[Throwable, Boolean] = Left(new ArithmeticException)

sealed trait MyError
case object DivZero extends MyError
final case class CustomError(message: String) extends MyError

type MathsOpResult = Either[MyError, Float]

val failableOp: MathsOpResult = Left(DivZero)

// we can abstract over error handling types (Try, Either, ...) with MonadError
import cats.instances.try_._  // for MonadError[Try]
import cats.syntax.applicativeError._ // for raiseError (N.B. not MonadError!)
import scala.util.Try

val exn: Throwable = new Exception("failure during calculation")
val tryResult = exn.raiseError[Try, Int]
