/** Traverse helps us iterate within some foldable context.
 **
 * Consider `Future` from the standard lib. It provides Future.traverse
  */
import cats.{Applicative, Foldable}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

val hosts: List[String] = List(
    "google.com",
    "microsoft.com",
    "bbc.co.uk"
  )

def lookup(host: String): Future[Int] =
    Future(200) // OK

val tmp = hosts.map(lookup) // Seq[Future[Int]] ! Many futures
tmp.map(Await.result(_, 1.second)) // have to map

// we can accumulate inside one Future by folding...
val allResults: Future[List[Int]] =
  hosts.foldLeft(Future(List.empty[Int]))((acc, host: String) => {
    for {
      resultCode <- lookup(host)
      acc <- acc
    } yield acc :+ resultCode
  })
Await.result(allResults, 1.second)

// This is all a bit verbose, because we have to traverse the list
// and combine all results.

// We can abstract away the requirement to combine results for
// any Applicative F[_]:
import scala.language.higherKinds
import cats.syntax.applicative._  // for pure
import cats.syntax.apply._  // for mapN
import cats.syntax.foldable._ // for foldl

def allResultsApp[A, B, F[_]: Applicative, C[_]: Foldable](items: C[A])(f: A => F[B]) =
  items.foldl(List.empty[B].pure[F]) { (acc, items) =>
    (acc, f(items)).mapN(_ :+ _)
    // recall that mapN is doing a product: (F[A], F[B]) => F[(A, B)]
    // this is equivalent to the for comprehension
  }

import cats.instances.list._
import cats.instances.future._
Await.result(allResultsApp(hosts)(lookup), 1.second)

// `traverse` abstracts this away. Future in the std lib has one:
Future.traverse(hosts)(lookup)

// cats provides a `traverse` typeclass.