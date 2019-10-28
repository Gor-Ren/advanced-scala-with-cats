package ch04_monads

import cats.Monoid
import cats.data.State
import cats.instances.list._
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class PostfixCalculatorSpec
    extends FunSuite
    with ScalaCheckDrivenPropertyChecks
    with Matchers {

  test("PostfixCalculator#evalOne adds an integer to the stack") {
    forAll { i: Int =>
      val result = PostfixCalculator().evalOne(i.toString).get
      runAndGetStack(result) should contain theSameElementsInOrderAs Seq(i)
    }
  }

  /** Runs the input calculation with an empty stack, returning the stack. */
  def runAndGetStack[S, ?](
      calculation: State[S, ?]
  )(implicit m: Monoid[S]): S = {
    calculation.runS(m.empty).value
  }
}
