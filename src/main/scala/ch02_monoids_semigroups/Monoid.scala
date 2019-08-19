package ch02_monoids_semigroups

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit monoid: Monoid[A]): Monoid[A] = monoid
}

object BoolAnd extends Monoid[Boolean] {
  override val empty: Boolean = true

  override def combine(x: Boolean, y: Boolean): Boolean =
    x && y
}

object BoolOr extends Monoid[Boolean] {
  override val empty: Boolean = false

  override def combine(x: Boolean, y: Boolean): Boolean =
    x || y
}

object BoolXor extends Monoid[Boolean] {
  override val empty: Boolean = false

  override def combine(x: Boolean, y: Boolean): Boolean =
    (x && !y) || (!x && y)
}

// TODO: test monoid laws hold
