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

// see TestBoolMonoids for proofs they behave correctly

class SetAdd[A] extends Monoid[Set[A]] {
  override def empty: Set[A] = Set.empty

  override def combine(x: Set[A], y: Set[A]): Set[A] = x ++ y
}

class SetUnion[A] extends Monoid[Set[A]] {
  override def empty: Set[A] = Set.empty

  override def combine(x: Set[A], y: Set[A]): Set[A] = x.union(y)
}

class SetIntersect[A] extends Semigroup[Set[A]] {
  override def combine(x: Set[A], y: Set[A]): Set[A] = x.intersect(y)
}

object MonoidExercise extends App {
  implicit val boolOrMonoid: Monoid[Boolean] = BoolOr

  implicit private class MonoidSyntax[A](value: A) { // private to avoid clashes
    def empty(implicit m: Monoid[A]): A = m.empty

    def combine(other: A)(implicit m: Monoid[A]): A =
      m.combine(value, other)
  }

  println(true.combine(true))

  // Alternatively, can write things like:
  Monoid[Boolean].empty
  // ... because an implicit Monoid[Boolean] is in scope, and the Monoid.apply
  // method will return it
}
