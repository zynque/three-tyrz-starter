package com.fractaltreehouse.threetyrz.components.data

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*

// A component that duplicates its output, emitting it twice, once as a Left and once as a Right
class Duplicated[F[_], I, O, M, S](
    component: DataComponent[F, I, O, M, S]
) extends DataComponent[
      F,
      I,
      Either[O, O],
      M,
      S
    ] {
  def init: (S, Cmd[F, M]) = component.init
  def update(state: S, value: Either[I, M]): (
      S,
      Cmd[F, Either[Either[O, O], M]]
  ) =
    component.update(state, value) match {
      case (s, cmd) =>
        val left = cmd.map {
          case Left(o)  => Left(Left(o))
          case Right(m) => Right(m)
        }
        val right = cmd.map {
          case Left(o)  => Left(Right(o))
          case Right(m) => Right(m)
        }
        (s, left |+| right)
    }
}
