package ch04_monads

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

import scala.util.Try

class PostfixCalculatorSpec
    extends FunSuite
    with ScalaCheckDrivenPropertyChecks
    with Matchers {

  implicit val arbOperators: Arbitrary[Operator] = Arbitrary(
    Gen.oneOf(Operator.operators.toSeq)
  )

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
    "PostfixCalculator#evalOne pops two values from the stack and pushes result"
  ) {
    forAll { (stack: List[Int], op: Operator) =>
      val shouldRun: Boolean = stack.size >= 2 && {
        // don't run arithmetic edge cases that cause a failure
        val a = stack.head
        val b = stack.tail.head
        Try(op.operation(a, b)).isSuccess
      }

      whenever(shouldRun) {
        val (finalStack, answer) = PostfixCalculator()
          .evalOne(Operator.add.symbol)
          .run(stack)
          .value
        answer shouldBe op.operation(stack.head, stack.tail.head)
        finalStack should contain theSameElementsInOrderAs
          (answer :: stack.drop(2))
        println(
          s"stack=$stack, operation=$op, finalStack=$finalStack, ans=$answer"
        )
      }
    }
  }
}

case class Operator(symbol: String, operation: (Int, Int) => Int)

object Operator {
  val add: Operator = new Operator("+", (x, y) => x + y)
  val subtract: Operator = new Operator("-", (x, y) => x + y)
  val multiply: Operator = new Operator("*", (x, y) => x + y)
  val divide: Operator = new Operator("/", (x, y) => x + y)

  val operators: Set[Operator] = Set(add, subtract, multiply, divide)
}
