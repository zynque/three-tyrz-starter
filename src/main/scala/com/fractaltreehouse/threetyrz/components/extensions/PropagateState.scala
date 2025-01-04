package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.Html
import tyrian.Cmd

// Transforms a component into one that always outputs the latest state (ignoring the output of the original component, if any)
class StatePropagated[F[_], I, O, M, S](
    component: TyrianComponent2[F, I, O, M, S]
) extends TyrianComponent2[F, I, S, M, S] {
  override val init: (S, Cmd[F, M]) = component.init
  override def update(
      state: S,
      value: Either[I, M]
  ): (S, Cmd[F, Either[S, M]]) = {
    val (s2, cmd) = component.update(state, value)
    val cmd2 = cmd.map {
      case Left(o)  => Left(s2)
      case Right(m) => Right(m)
    }
    (s2, cmd2)
  }
  override def view(state: S): Html[M] = component.view(state)
}
