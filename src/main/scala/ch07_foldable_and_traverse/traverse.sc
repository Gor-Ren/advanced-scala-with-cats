/** Traverse helps us iterate within some foldable context.

  Consider `Future` from the standard lib. It provides Future.traverse
 */
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

val hosts: Seq[String] = Seq(
  "google.com",
  "microsoft.com",
  "bbc.co.uk"
)

def lookup(host: String): Future[Int] =
  Future(200)  // OK

hosts.map(lookup)  // Seq[Future[Int]] ! Many futures

// we can accumulate inside one Future by folding...
hosts.fold(Future(List.empty))((acc, host) =>
  for {
    results <- acc
    x <- lookup(host)
  } yield x :+ 
)