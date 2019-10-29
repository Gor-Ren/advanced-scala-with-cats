package ch04_monads

import cats.data.State
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class PostfixCalculatorSpec
    extends FunSuite
    with ScalaCheckDrivenPropertyChecks
    with Matchers {

  test("PostfixCalculator#evalOne adds an integer to the top of the stack") {
    forAll { (i: Int, stack: List[Int]) =>
      PostfixCalculator()
        .evalOne(i.toString)
        .runS(stack)
        .value should contain theSameElementsInOrderAs (i :: stack)
    }
  }

  test("PostfixCalculator#evalOne returns an integer as the result") {
    forAll { (i: Int, stack: List[Int]) =>
      PostfixCalculator()
        .evalOne(i.toString)
        .runA(stack)
        .value shouldBe i
    }
  }

  test(
    "PostfixCalculator#evalOne pops two ints from the stack and pushes the result"
  ) {
    forAll { (i: Int, stack: List[Int]) =>
      PostfixCalculator()
        .evalOne(i.toString)
        .runA(stack)
        .value shouldBe i
    }
  }

  /** Runs the input calculation with the input stack, returning the stack. */
  def runAndGetStack(initialStack: List[Int],
                     calculation: State[List[Int], Int]): List[Int] = {
    calculation.runS(initialStack).value
  }
}
