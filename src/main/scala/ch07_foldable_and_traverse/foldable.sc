/**
 * Foldable
 *
 * A type class capturing the foldLeft and foldRight methods.
 *
 * Lets us write e.g. folds that work with generic Sequences.
 */

// folding: we provide an accumulator value and a binary function
def sum(is: List[Int]): Int =
  is.fold(0)(_ + _)

// we can control the traversal direction using foldLeft or foldRight
// they are equivalent if our binary function is associative (e.g. _ + _)
// but not if the function isn't associative (e.g. _ - _)

// (side note: they are always equivalent for Monoids, then, which must be
// associative).

/** Exercise: folding List prepends */
def copyLeft[T](list: List[T]): List[T] =
  list.foldLeft(List.empty[T])((xs, x) => x :: xs)

def copyRight[T](list: List[T]): List[T] =
  list.foldRight(List.empty[T])((x, xs) => x :: xs)

copyLeft(List(1, 2, 3)) // reverses the list
copyRight(List(1, 2, 3))

/** Exercise: implement List[Int] map, flatmap, filter as folds */
def map[A](list: List[Int], f: Int => A): List[A] =
  list.foldRight(List.empty[A])((x, xs) => f(x) :: xs)

map(List(1, 2), _.toString)

def flatMap[A](list: List[Int], f: Int => List[A]): List[A] =
  list.foldRight(List.empty[A])((x, xs) => f(x) ::: xs)

flatMap(List(1, 2), (i: Int) => List(i + 10, i + 11))

def filter[A](list: List[Int], p: Int => Boolean): List[Int] =
  list.foldRight(List.empty[Int])((x, xs) =>
    if (p(x)) {
      x :: xs
  } else {
      xs
  })

def isEven(i: Int): Boolean = (i % 2) == 0
filter(List(1, 2, 3), isEven)

/** Cats Foldable: abstracts foldLeft and foldRight */
import cats.Foldable
import cats.instances.list._
val ints = List(1, 2, 3)
Foldable[List].foldLeft(ints, 0)(_ + _)