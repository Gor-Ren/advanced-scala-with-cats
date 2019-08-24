package ch03_functors

// imap: generates type classes using bidirectional transformation functions

trait Codec[A] {
  self =>

  def encode(value: A): String
  def decode(value: String): A

  def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {
    override def encode(value: B): String = self.encode(enc(value))

    override def decode(value: String): B = dec(self.decode(value))
  }
}

object CodecExercises extends App {
  implicit val stringCodec: Codec[String] =
    new Codec[String] {
      def encode(value: String): String = value
      def decode(value: String): String = value
    }

  // define new codec by imapping from String
  implicit val doubleCodec: Codec[Double] =
    stringCodec.imap(
      _.toDouble,
      _.toString
    )

  case class Box[A](value: A)

  implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] =
    c.imap[Box[A]](
      Box(_),
      _.value
    )

  println(boxCodec[Double].encode(Box(2d)))
  println(boxCodec[Double].decode("999d"))
}
