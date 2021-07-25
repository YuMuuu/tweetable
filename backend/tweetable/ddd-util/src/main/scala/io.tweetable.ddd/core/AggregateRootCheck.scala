package io.tweetable.ddd.core

trait AggregateRootCheck[A]

object AggregateRootCheck:
  import scala.deriving.Mirror
  import scala.compiletime.{constValue, erasedValue, error, summonInline}
  inline def checkElem[T](): Unit =
    inline erasedValue[T] match
      case _: AggregateRootEntity[?] =>
        error("AggregateRoot can't have another AggregateRoot")
      case _ => ()

  inline def checkElems[Elems <: Tuple](): Unit =
    inline erasedValue[Elems] match
      case _: (t *: ts) => {
        checkElem[t]()
        checkElems[ts]()
      }
      case _ => ()

  inline given derived[T <: AggregateRootEntity[?]](
      using ev: Mirror.Of[T]
  ): AggregateRootCheck[T] =
    new AggregateRootCheck[T]:
      inline ev match
        case m: Mirror.ProductOf[T] =>
          checkElems[m.MirroredElemTypes]()
        case _ => ()
