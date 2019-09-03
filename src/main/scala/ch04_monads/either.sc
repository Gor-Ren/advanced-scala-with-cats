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

/** Divide x by all values in divs, returning result or error */
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
