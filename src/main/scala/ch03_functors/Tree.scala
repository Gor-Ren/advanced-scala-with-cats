package ch03_functors

import cats.Functor
import cats.syntax.functor._ // for map

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  implicit val TreeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](tree: Tree[A])(f: A => B): Tree[B] =
      tree match {
        case Leaf(x) => Leaf(f(x))
        case Branch(l, r) =>
          Branch(
            map(l)(f),
            map(r)(f)
          )
      }
  }

  // convenience methods to create branches/leafs typed as `Tree`
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  def leaf[A](value: A): Tree[A] = Leaf(value)
}

object TreeExercise extends App {

  val testTree: Tree[String] =
    Branch(
      Leaf("one"),
      Branch(
        Leaf("two"),
        Leaf("three")
      )
    )
  val incredulousTree = testTree.map(_ + "??!")
  println(incredulousTree)
}
