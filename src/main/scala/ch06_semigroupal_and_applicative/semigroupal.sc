// semigroup allows us to combine two values
// semigroupal allows us to combine two contexts

import cats.Semigroupal
import cats.instances.option._ // for Semigroupal

Semigroupal[Option].product(Some(1), Some("a"))  // Some((1, "a"))
Semigroupal[Option].product(Some(1), None)  // None

// `product` is generalised to a tuple up to size 22 using
// `tuple2` to `tuple22`
Semigroupal.tuple3(Option(1), Option(2), Option(3))
Semigroupal.tuple3(Option(1), Option(2), None) // None

// same with mapN
Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)

// Cats provides an `apply` syntax for the above:
import cats.syntax.apply._

(Option(123), Option('a')).tupled

// mapN is a cool way to input multiple contexts to a function in
// a type safe way. It needs a Functor[F] in addition to the Semigroupal[F]
case class Cat(name: String, born: Int, colour: String)

(Option("Garfield"), Option(1978), Option("Orange and black"))
  .mapN(Cat)  // an Option of Garfield

(None, Option(1978), Option("Orange and black"))
  .mapN(Cat)  // a None

/** Semigroupal of other types (than Option) */
// Semiigroupal "doesn't always provide the behaviour we expect"
// "particularly for types that also have instances of Monad".

/* Future */
// performs parallel rather than sequential execution

import cats.instances.future._
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.higherKinds

val futurePair = Semigroupal[Future]
  .product(Future("hi"), Future(42))

Await.result(futurePair, 1.second)

val futureCat =
  (Future("Garfield"), Future(1978), Future("Orange and black"))
    .mapN(Cat)
Await.result(futureCat, 1.second)

/* List */
// combining Lists with Semigroupal gets their Cartesian product
import cats.instances.list._

Semigroupal[List].product(List(1, 2), List("a", "b"))
// so this is like flatMapping on them, not Monoid.combine()ing them

/* Either */
// Either's Semigroupal implements the normal fail fast behaviour!
// i.e. it ignores later failure behaviour to report only the first

import cats.instances.either._

type ErrorOr[A] = Either[Vector[String], A]

Semigroupal[ErrorOr].product(
  Left(Vector("first error")), Left(Vector("second error"))
)  // only reports first error!

/* Reason */
// the cause of the above behaviour is that they are both Monads,
// and Cat's Monad extends Semigroupal. `product` is implemented
// in terms of `map` and `flatMap`.
// The only reason Futures are evaluated in parallel is because they
// use eager execution, and are created before `product` is called!

// there are times when we can create a Semigroupal (or Applicative)
// for a data type which is NOT a Monad; that's why we have them.

/** 6.3.1.1 Exercise: The Product of Monads */
// implement `product` in terms of flatMap

import cats.Monad

// first attempt: didn't have the right imports for a comprehension
def productVerbose[M[_]: Monad, A, B](fa: M[A], fb: M[B]): M[(A, B)] =
  Monad[M].flatMap(fa)(a =>
    Monad[M].flatMap(fb)(b =>
      Monad[M].pure(a, b)
    )
  )

import cats.syntax.flatMap._ // for flatMap
import cats.syntax.functor._ // for map
// with the above imports this now works
def product[M[_]: Monad, A, B](fa: M[A], fb: M[B]): M[(A, B)] =
  for {
    a <- fa
    b <- fb
  } yield (a, b)
