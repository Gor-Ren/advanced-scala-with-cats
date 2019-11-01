/* Exercise: write a Monad for Tree class */
import ch03_functors.{Branch, Leaf, Tree}
import cats.Monad

class TreeMonad extends Monad[Tree] {
  override def flatMap[A, B](fa: Tree[A])(f: A => Tree[B]): Tree[B] =
    fa match {
      case Branch(left, right) => Branch(flatMap(left)(f), flatMap(right)(f))
      case Leaf(value)         => f(value)
    }

  override def tailRecM[A, B](a: A)(f: A => Tree[Either[A, B]]) = ???
  // TODO: read Phil Freeman's paper and implement!

  override def pure[A](x: A) = Leaf(x)
}
