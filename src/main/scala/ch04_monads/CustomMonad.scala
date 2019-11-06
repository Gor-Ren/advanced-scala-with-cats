package ch04_monads
/* Exercise: write a Monad for Tree class */
import ch03_functors.{Branch, Leaf, Tree}
import cats.Monad
import cats.implicits._

class TreeMonad extends Monad[Tree] {

  override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
    fa match {
      case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
      case Leaf(value)         => f(value)
    }

  // this is not stack safe. A stack safe solution is quite complex for a Tree.
  // the intention of this method is that you provide a tail-recursive impl
  // which allows Cats to guarantee stack safety.
  override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]): Tree[B] = {
    flatMap(f(a)) {
      case Left(a)  => tailRecM(a)(f)
      case Right(b) => Leaf(b)
    }
  }

  override def pure[A](x: A) = Leaf(x)
}

object MonadPlay extends App {
  import cats.syntax.functor._
  implicit val treeMonad: Monad[Tree] = new TreeMonad

  // had to prevent type inference narrowing from Tree to Branch
  val tree: Tree[Int] = Branch(Leaf(1), Leaf(2))
  // now it's a Tree, can use functor ops
  tree.map(_ + 1)

  // by implementing Monad, we got Functor for free!
  for {
    a <- Tree.branch(Leaf(1), Leaf(2))
    b <- Tree.leaf(a + 10)
    c <- Tree.branch(Leaf(b + 100), Leaf(b - 100))
  } yield println(c)
}
