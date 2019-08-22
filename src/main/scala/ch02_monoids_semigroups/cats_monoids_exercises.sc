import cats.Monoid
import cats.instances.int._ // for monoid
import cats.instances.option._ // for monoid
import cats.syntax.monoid._ // for |+|

object SuperAdder {

  def add(items: List[Int]): Int =
    items.foldLeft(Monoid[Int].empty)(_ |+| _) // this looks very generalisable!

  def add[A: Monoid](items: List[A]): A =
  // equiv to `def add[A](items: List[A])(implicit m: Monoid[A]): ...`
  // except we don't get a reference to the monoid `m`!
    items.foldLeft(Monoid[A].empty)(_ |+| _)
}

val myList = List(1, 2, 3)

SuperAdder.add(myList)

val myOptList = myList.map(Option(_))
SuperAdder.add(myOptList)

SuperAdder.add(None +: myOptList)


/* Exercise: expand to new class */
case class Order(totalCost: Double, quantity: Double)

implicit val orderMonoid: Monoid[Order] = new Monoid[Order] {
  override def empty = Order(0, 0)

  override def combine(x: Order, y: Order) =
    Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
}

SuperAdder.add(List(Order(10, 1), Order (1, 1)))
