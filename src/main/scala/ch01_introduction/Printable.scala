package ch01_introduction

trait Printable[A] {
  def format(value: A): String
}

object PrintableInstances {
  implicit val printableString: Printable[String] = (value: String) => value

  implicit val printableInt: Printable[Int] = (value: Int) => value.toString
}

object Printable {

  def format[A](value: A)(implicit printer: Printable[A]): String =
    printer.format(value)

  def print[A](value: A)(implicit printer: Printable[A]): Unit =
    println(format(value))
}

final case class Cat(name: String, age: Int, colour: String)

// decided to place the type class instance in the companion object
object Cat {
  import PrintableInstances.{printableInt, printableString}

  implicit val printableCat: Printable[Cat] = (cat: Cat) =>
    s"${Printable.format(cat.name)} is a " +
      s"${Printable.format(cat.age)} year-old " +
      s"${Printable.format(cat.colour)} cat."
}

object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {

    def format(implicit p: Printable[A]): String =
      p.format(value)

    def print()(implicit p: Printable[A]): Unit =
      println(format)
  }
}

object PrintableExercise extends App {
  /* First Exercise: Print a cat */
  val cat: Cat = Cat("Nina", 4, "black")

  import PrintableInstances._
  Printable.print(cat)

  /* Second Exercise: Print cat using extension method (prettier syntax) */
  import PrintableSyntax._
  cat.print()

  /* Third exercise: use Cats instead! */
  import cats.Show
  import cats.instances.int._
  import cats.instances.string._
  import cats.syntax.show._

  implicit val showCat: Show[Cat] = Show.show(
    (cat: Cat) =>
      s"${cat.name.show} is a " +
        s"${cat.age.show} year-old " +
        s"${cat.colour.show} cat."
  )

  println(cat.show)
}
