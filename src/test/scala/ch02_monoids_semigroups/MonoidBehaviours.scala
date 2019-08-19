package ch02_monoids_semigroups

import org.scalatest.FlatSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

trait MonoidBehaviours { this: FlatSpec =>

  val allBools: Set[Boolean] = Set(true, false)

  val allBoolTriples: Set[(Boolean, Boolean, Boolean)] =
    for {
      b1 <- allBools
      b2 <- allBools
      b3 <- allBools
    } yield (b1, b2, b3)

  def booleanMonoid(m: => Monoid[Boolean]): Unit = {

    it should "be associative" in {
      allBoolTriples.foreach {
        case (a, b, c) =>
          assert(
            m.combine(m.combine(a, b), c) === m.combine(a, m.combine(b, c))
          )
      }
    }

    it should "have an identity value" in {
      allBools.foreach(b => assert(m.combine(b, m.empty) === b))
      allBools.foreach(b => assert(m.combine(m.empty, b) === b))
    }
  }
}
