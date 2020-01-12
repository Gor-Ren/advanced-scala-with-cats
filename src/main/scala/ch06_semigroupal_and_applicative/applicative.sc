/** Applicative functors provide a superset of the functionality of a Semigroupal.
  *
  * They are more frequently referenced than Semigroupals.
  *
  * Cats has two relevant typeclasses:
  * - Apply, which extends Semigroupal and Functor
  */
import cats.Semigroupal
import cats.Functor

trait MyApply[F[_]] extends Semigroupal[F] with Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]

  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
}

/**
  * - Applicative, which extends apply
  */
trait MyApplicative[F[_]] extends MyApply[F] {
  def pure[A](a: A): F[A]
  // the same pure as we saw in Monad
  // Applicative is to Apply and Monoid is to Semigroup
}

/**
  * ap, map and product are tightly related, such that an implementation of each
  * can be derived from the other two.
  */

/**
  * Applicative vs Monad
  *
  * Monads allow us to flatMap, but are limited to obey additional laws: they can
  * only represent sequential computations. Applicatives lack flatMap but are
  * more flexible and for combining multiple values, such as running independent
  * calculations in parallel and/or accumulating validation results.
  */
