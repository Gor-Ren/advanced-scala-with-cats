/*
  Functors allow us to sequence computation by `map`ping.
 */

trait MyFunctor[F[_]] { // "some type F for which we have a single-param constructor"
  def map[A, B](fa: F[A], f: A => B): F[B]
}
