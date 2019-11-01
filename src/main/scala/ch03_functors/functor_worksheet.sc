/*
  Functors allow us to sequence computation by `map`ping.

  A functor is a type F[A] providing a method (A => B) => F[B].

  We can think of it as appending a transformation to a chain.

  It obeys two laws:
    - Identity: mapping the identity function x => x is the same as doing nothing
    - Composition: mapping with f and g (e.g. `g(f(_))` is the same as mapping
                   with f then mapping with g
 */

trait MyFunctor[F[_]] { // "some type F for which we have a single-param constructor"
  def map[A, B](fa: F[A], f: A => B): F[B]
}

import cats.Functor
import cats.instances.list._ // for Functor
import cats.instances.option._ // for Functor

val list1 = List(1, 2, 3)

Functor[List].map(list1)(_ + 1)
  .map(String.valueOf)

val f: Int => String = x => String.valueOf(x -10)

/* lifts an f: A => B to an F[A] => F[B] */
Functor[Option].lift(f).apply(None)
Functor[Option].lift(f).apply(Some(0))

import cats.syntax.functor._ // for map

// we can write generic expressions for any functor:
def doMath[F[_]: Functor](start: F[Int]): F[Int] =
  start.map(n => n + 1 + 2)

doMath(List(1, 2, 3))