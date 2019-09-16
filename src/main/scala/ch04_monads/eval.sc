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

// we can force it to memoise:
res.map(_ + 1).memoize.value
res.value  