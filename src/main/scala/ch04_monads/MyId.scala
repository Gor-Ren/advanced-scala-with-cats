package ch04_monads

import cats.Id

object IdMethods {

  def pure[A](a: A): Id[A] = a

  def flatMap[A, B](value: Id[A])(f: A => Id[B]): Id[B] =
    f(value)

  def map[A, B](value: Id[A])(f: A => B): Id[B] =
    f(value)
}
