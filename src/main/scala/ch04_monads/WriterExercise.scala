package ch04_monads

import cats.data.Writer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
//import cats.implicits._
import cats.instances.vector._
import cats.instances.int._
import cats.instances.string._
import cats.instances.tuple._

import cats.syntax.show._
import cats.syntax.writer._
import cats.syntax.applicative._

object WriterExercise extends App {

  type Logged[A] = Writer[Vector[String], A]

  def slowly[A](body: => A) =
    try body
    finally Thread.sleep(100)

  def factorial(n: Int): Logged[Int] = {
    slowly(
      for {
        ans <- if (n == 0)
          1.pure[Logged]
        else
          factorial(n - 1).map(_ * n)
        _ <- Vector(s"fact $n = $ans").tell
      } yield ans
    )
  }

  println(factorial(5).show)
  println(factorial(3).show)

  val results =
    Await.result(Future.sequence(
                   Vector(
                     Future(factorial(3).run),
                     Future(factorial(5).run)
                   )
                 ),
                 5.seconds)

  // keeps separate logs for the interleaved computations
  println(results.show)
}
