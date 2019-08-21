package ch02_monoids_semigroups

import org.scalatest.FlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

trait BoolMonoidBehaviours extends ScalaCheckDrivenPropertyChecks {
  this: FlatSpec =>

  val allBools: Set[Boolean] = Set(true, false)

  val allBoolTriples: Set[(Boolean, Boolean, Boolean)] =
    for {
      b1 <- allBools
      b2 <- allBools
      b3 <- allBools
    } yield (b1, b2, b3)

  def booleanMonoid(m: => Monoid[Boolean]): Unit = {

    it should "obey the associative law" in {
      allBoolTriples.forall {
        case (a, b, c) =>
          m.combine(m.combine(a, b), c) === m.combine(a, m.combine(b, c))
      }
    }

    it should "obey the identity law" in {
      allBools.forall(b => m.combine(b, m.empty) === b)
      allBools.forall(b => m.combine(m.empty, b) === b)
    }
  }
}
