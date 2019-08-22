import cats.Monoid
import cats.instances.string._ // for Monoid
import cats.instances.int._ // for Monoid
import cats.instances.option._ // for Monoid
import cats.syntax.semigroup._ // for |+|

Monoid[String].combine("hello ", "there")

"what " |+| "up" |+| Monoid[String].empty

val optSum = Option(1) |+| Option(3)

optSum |+| None  // behaviour: doesn't add anything
