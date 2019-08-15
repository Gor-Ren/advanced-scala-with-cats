package ch01_introduction
import cats.Eq
import cats.instances.string._ // for Eq
import cats.instances.int._ // for Eq
import cats.instances.option._ // for Eq
import cats.syntax.eq._

object CatEq extends App {
  /* Exercise code */
  val cat1 = Cat("Garfield", 38, "orange and black")
  val cat2 = Cat("Heathcliff", 33, "orange and black")
  val optionCat1 = Option(cat1)
  val optionCat2 = Option.empty[Cat]

  // my code
  implicit val catEq: Eq[Cat] = Eq.instance[Cat](
    (cat1, cat2) =>
      cat1.name === cat2.name
        && cat1.age === cat2.age
        && cat1.colour === cat2.colour
  )

  println(cat1 === cat2)
  println(cat1 =!= cat2)

  println(optionCat1 === optionCat2)
  println(optionCat1 =!= optionCat2)
}
