package ch04_monads

import cats.Id

// exercise 4.3
object IdMethods {

  def pure[A](a: A): Id[A] = a // works because Id[A] === A

  def map[A, B](value: Id[A])(f: A => B): Id[B] =
    f(value)

  // consider that the Id Monad "encodes the effect of having no effect", then
  // flatMapping on it is the same as mapping, is the same as applying the input
  // f to the value
  def flatMap[A, B](value: Id[A])(f: A => Id[B]): Id[B] =
    f(value)
}
