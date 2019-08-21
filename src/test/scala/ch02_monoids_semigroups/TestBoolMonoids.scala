package ch02_monoids_semigroups

import org.scalatest.FlatSpec

class TestBoolMonoids extends FlatSpec with BoolMonoidBehaviours {

  "Boolean AND" should behave like booleanMonoid(BoolAnd)

  "Boolean OR" should behave like booleanMonoid(BoolOr)

  "Boolean XOR" should behave like booleanMonoid(BoolXor)
}
