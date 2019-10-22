package ch04_monads

import cats.data.State

/** A calculator for integer arithmetic using postfix notation. */
class PostfixCalculator {
  type CalculatorState[A] = State[List[Int], A]

  val state: CalculatorState[Int] = State.pure(0)

  def evalOne(sym: String): CalculatorState[Int] =
    if (Operators.isOperator(sym)) {
      val op = Operators.toOperator(sym).get
      for {
        el1 <- pop
        el2 <- pop
      } yield op.calculate(el1, el2)
    } else {
      // must be a number (or throw)
      state.modify(sym.toInt :: _)
    }

  private def pop: CalculatorState[Int] = {
    State[List[Int], Int] {
      case h :: tail => (tail, h)
      case _         => throw new RuntimeException("stack is empty!")
    }
  }
}

object PostfixCalculator {
  def apply() = new PostfixCalculator
}

object Operators {

  def toOperator(sym: String): Option[PostFixBinaryOperator] =
    sym match {
      case "+" => Some(Add)
      case "-" => Some(Subtract)
      case "*" => Some(Multiply)
      case "/" => Some(Divide)
      case _   => None
    }

  def isOperator(sym: String): Boolean = toOperator(sym).isDefined
}

sealed trait PostFixBinaryOperator {
  def calculate(a: Int, b: Int): Int
}

object Add extends PostFixBinaryOperator {
  override def calculate(a: Int, b: Int): Int = a + b
}

object Subtract extends PostFixBinaryOperator {
  override def calculate(a: Int, b: Int): Int = a - b
}

object Multiply extends PostFixBinaryOperator {
  override def calculate(a: Int, b: Int): Int = a * b
}

object Divide extends PostFixBinaryOperator {
  override def calculate(a: Int, b: Int): Int = a / b
}
