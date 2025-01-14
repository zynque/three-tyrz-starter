package com.fractaltreehouse.threetyrz.components.data

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*

// Combines two components into one, with the output of the new component being either the output of the first component or the output of the second component
// For example, multiple text boxes can be combined into a single component that emits the value of the text box that was most recently changed
class Duplicated[F[_], I, O, M, S](
    component: DataComponent[F, I, O, M, S],
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
  ) = {
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
}
