import cats.Eval

val eagerVal = Eval.now(math.random())
val lazyMemoisedVal = Eval.later(math.random())
val lazyNotMemoisedVal = Eval.always(math.random())

for {
  x <- eagerVal
  cached1 <- lazyMemoisedVal
  cached2 <- lazyMemoisedVal
  notCached1 <- lazyNotMemoisedVal
  notCached2 <- lazyNotMemoisedVal
} yield println(
    s"eager: $x\n",
    s"lazy memoised 1: $cached1\n",
    s"lazy memoised 2: $cached2\n",
    s"lazy not memoised 1: $notCached1\n",
    s"lazy not memoised 2: $notCached2\n"
)

// `map` calls are always lazy and not memoised
Eval.now(1)
  .map(_ + 1)

val res = for {
  a <- Eval.now(1)
  b <- Eval.now(2)
} yield {
  println("woo, side effect")
  a + b
}  // yields a lazy non-memoised Eval

res.value
res.value  // re-runs side effect

// we can force it to memoise in a chain:
val memo = Eval.always(42)
    .map { s => println("side effect"); s + 1}
    .memoize
    .map(_ + 1)

memo.value // with side effect
memo.value // computation result was memoised; no side effect

// map and flatMap are trampolined, providing stack safety
// we can use Eval.defer to trampoline computations which produce an eval:

/** Exercise */
def foldRightEval[A, B](as: List[A], acc: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] =
  as match {
    case head :: tail => Eval.defer(
      Eval.defer(
          f(head, foldRightEval(tail, acc)(f))
      )
    )
    case Nil => acc
  }

def foldRight[A, B](as: List[A], acc: B)(f: (A, B) => B): B =
  foldRightEval(as, Eval.now(acc)) {(a, b) =>
    b.map(f(a, _))
  }.value

// this is still throwing SO for me, despite being the book's solution!
foldRight((1L to 50000L).toList, 0L)(_ + _)
