package ch05_monad_transformers

import cats.data.EitherT
import cats.instances.future._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.global

class MonadT {
  implicit val ec: ExecutionContext = global

  // a response is a fallible async call
  type Response[A] = EitherT[Future, String, A]

  def getPowerLevel(autobot: String): Response[Int] =
    EitherT(
      Future(
        PowerData.powerLevels
          .get(autobot)
          .toRight(s"No power level response from autobot: $autobot")
      )
    )
}

object PowerData {

  val powerLevels: Map[String, Int] = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )
}
