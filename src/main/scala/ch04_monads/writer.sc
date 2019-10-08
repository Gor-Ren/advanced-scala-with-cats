import cats.data.Writer
import cats.instances.vector._
// a Writer contains a log of type W and a result, A
val writer0: Writer[Vector[String], Int] = Writer.value(123)  // a result 123

// note: Writer[A, B] is an alias for WriterT[Id, A, B] i.e. effectless

// making a log-only Writer with no result using syntax:
import cats.syntax.writer._
val writer1: Writer[Vector[String], Unit] = Vector("log1", "log2", "end").tell

// or we can have both result and log:
val writerBoth1 = Writer(Vector("one", "two"), 42)
val writerBoth2 = 55.writer(Vector("start", "end"))

// ... and we can retrieve data from them:
val result: Int = writerBoth1.value
val log = writerBoth1.written  // if we weren't using Id, we'd get an effect back
// both at same time:
val (r, l) = writerBoth1.run

// a Writer preserves its log as we map and flatMap over it
// it appends new logs to the existing, so we should choose a log type with
// efficient appends (like Vector)