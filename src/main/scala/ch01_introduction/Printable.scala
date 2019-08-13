package ch01_introduction

trait Printable[A] {
  def format(value: A): String
}
