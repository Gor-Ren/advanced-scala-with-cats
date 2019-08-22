import cats.Monoid
import cats.instances.int._ // for monoid
import cats.syntax.monoid._ // for |+|

object SuperAdder {

  def add(items: List[Int]): Int =
    items match {
      case head :: tail => head |+| add(tail)
      case Nil          => Monoid[Int].empty
    }
}
