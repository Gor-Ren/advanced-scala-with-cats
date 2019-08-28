package ch04_monads

/*
  Monads allow us to sequence computations. They provide a constructor and a
  flatmap method..

  Hey! Sequencing computations is how we described Functors! The difference is
  that functors let us sequence a computation, taking account of some
  complication once at the beginning of the sequence. Monads let us take account
  of intermediate complications throughout the sequence of computation.

  Monads provide the operations:
    - pure, A => F[A] (an abstraction over constructors; creates a monadic
                       context from a plain value)
    - flatMap, (F[A], A => F[B]) => F[B] (takes A from its context, performs computation)

  Monads obey the laws:
    - left identity: calling pure and flatMapping f is the same as applying f:
        pure(a).flatMap(f) == f(a)
    - right identity: passing pure to flatMap is an identity function
        m.flatMap(pure) == m
    - associativity: flatMapping over f and g is the same as flatMapping over f
                     and then flatMapping over g:
        m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))

   Monads are also Functors because `map` can be implemented easily from pure
   and flatMap.
 */

trait MyMonad[F[_]] {
  def pure[A](a: A): F[A]

  def flatMap[A, B](value: F[A])(f: A => F[B]): F[B]

  def map[A, B](value: F[A])(f: A => B): F[B] =
    flatMap(value)(a => pure(f(a)))
}

/* Some scrap code comparing map to flatMap. */
object MyMonadComparison extends App {
  val test1: Option[Option[Int]] = None
  val test2: Option[Option[Int]] = Some(None)
  val test3: Option[Option[Int]] = Some(Some(1))

  println(test1.map(x => x.map(_ + 1)))
  println(test2.map(x => x.map(_ + 1)))
  println(test3.map(x => x.map(_ + 1)))

  // flatMap pulls the value out of the monad context. and generates the next
  // context in the sequence
  println(test1.flatMap(x => x.map(_ + 1)))
  println(test2.flatMap(x => x.map(_ + 1)))
  println(test3.flatMap(x => x.map(_ + 1)))
}
