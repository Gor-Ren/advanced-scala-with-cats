package ch04_monads

import cats.Eval

object EvalExercise extends App {

  def foldRightEval[A, B](as: List[A],
                          acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] =
    as match {
      case Nil => acc
      // Eval.defer will use trampolining to eliminate the need to use stack frames
      case head :: tail => Eval.defer(fn(head, foldRightEval(tail, acc)(fn)))
    }

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    foldRightEval(as, Eval.now(acc))((a, b) => b.map(fn(a, _))).value

  // we can safely foldRight on a very large list:
  println(foldRight((1 to 100000).toList, 0L)(_ + _))
}
