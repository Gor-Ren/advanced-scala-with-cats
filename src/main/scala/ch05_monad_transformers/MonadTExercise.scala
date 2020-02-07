package ch05_monad_transformers

import cats.data.EitherT
import cats.instances.future._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration._

class MonadT {
  implicit val ec: ExecutionContext = global

  // a response is a fallible async call
  type Response[A] = EitherT[Future, String, A]

  def getPowerLevel(ally: String): Response[Int] =
    EitherT(
      Future(
        PowerData.powerLevels
          .get(ally)
          .toRight(s"No power level response from autobot: $ally")
      )
    )

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] =
    for {
      power1 <- getPowerLevel(ally1)
      power2 <- getPowerLevel(ally2)
    } yield power1 + power2 >= PowerData.SPECIAL_MOVE_POWER_REQ

  def tacticalReport(ally1: String, ally2: String): String =
    Await.result(canSpecialMove(ally1, ally2).value, 1 second) match {
      case Left(msg)    => msg
      case Right(false) => s"$ally1 and $ally2 cannot perform a special move"
      case Right(true)  => s"$ally1 and $ally2 can perform a special move"
    }
}

object PowerData {

  val SPECIAL_MOVE_POWER_REQ: Int = 15

  val powerLevels: Map[String, Int] = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )
}
