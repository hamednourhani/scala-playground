package ir.itstar.playground.typeClasses

import scala.language.experimental.macros

import magnolia._

object MagnoliaSample {

  trait Show[T] {
    def show(value: T): String
  }

  object ShowDerivation {
    type TypeClass[T] = Show[T]

    def combine[T](ctx: CaseClass[Show, T]): Show[T] = new Show[T] {

      def show(value: T): String =
        ctx.parameters
          .map { p => s"${p.label}=${p.typeclass.show(p.dereference(value))}"
          }
          .mkString("{", ",", "}")
    }

    def dispatch[T](ctx: SealedTrait[Show, T]): Show[T] =
      new Show[T] {

        def show(value: T): String = ctx.dispatch(value) { sub =>
          sub.typeclass.show(sub.cast(value))

        }
      }

    implicit def gen[T]: Show[T] = macro Magnolia.gen[T]
  }

}
