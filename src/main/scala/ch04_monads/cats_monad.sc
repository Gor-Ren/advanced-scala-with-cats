import cats.syntax.functor._ // provides map
import cats.syntax.flatMap._ // provides flatMap
import cats.syntax.applicative._ // provides pure

import cats.instances.option._
import cats.instances.list._

import cats.Monad


1.pure[Option]

5.pure[List]

// a function that works for any monad-wrapped int
def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
  for {
    x <- a
    y <- b
  } yield x*x + y*y

/* The Identity Monad */

// wraps a value in a monad, allowing us to abstract over monadic and
// non-monadic code.

// e.g. sumSquare(2, 3) doesn't compile, but
import cats.Id
import cats.instances.int._
sumSquare(Monad[Id].pure(1), Monad[Id].pure(1))
// does!

// Id[A] is just an alias for a type [A] so we can cast to it:
val wrappedInt: Id[Int] = 3

// this is handy for tests when we don't need to wrap the value in the monad
// used for prod code
