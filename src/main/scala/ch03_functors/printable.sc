import ch01_introduction.Printable

/* Addition chap 3.6 exercises */
implicit val stringPrintable: Printable[String] =
  (value: String) => "\"" + value + "\""

implicit val boolPrintable: Printable[Boolean] =
  b => if (b) "yes" else "no"

import ch01_introduction.PrintableSyntax._

"hi".print

final case class Box[A](value: A)

// having compile issues, even using official solution; may be related to
// use of Scala worksheets, resulting in wonky packages / imports?

//// if we know how to get the A out the box (Box[A] => A) and we have a
//// Printable[A], then using contramap we have a Printable[Box[A]]
//implicit def boxPrintable[A](implicit p: Printable[A]): Printable[Box[A]] =
//  p.contramap[Box[A]](_.value)
//
//Box(true).print()
//val quote: Box[String] = Box("what's in the box?!")
//quote.print()
