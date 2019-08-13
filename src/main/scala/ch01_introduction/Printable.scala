package ch01_introduction

trait Printable[A] {
  def format(value: A): String
}

object PrintableInstances {
  implicit val printableString: Printable[String] = (value: String) => value

  implicit val printableInt: Printable[Int] = (value: Int) => value.toString

  implicit val printableCat: Printable[Cat] = new Printable[Cat] {
    override def format(cat: Cat): String =
      Printable.format(cat.name) + "is a " + Printable.format(cat.age) +
        "year-old " + Printable.format(cat.colour) + " cat."
  }
}

object Printable {

  def format[A](value: A)(implicit printer: Printable[A]): String =
    printer.format(value)

  def print[A](value: A)(implicit printer: Printable[A]): Unit =
    println(format(value))
}

final case class Cat(name: String, age: Int, colour: String)
