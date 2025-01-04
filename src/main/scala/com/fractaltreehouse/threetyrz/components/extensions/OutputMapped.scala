package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.Html
import tyrian.Cmd

// Transforms a component into one that outputs a transformed output
class OutputMapped[F[_], I, O, M, S, O2](
    component: TyrianComponent2[F, I, O, M, S],
    f: O => O2
) extends TyrianComponent2[F, I, O2, M, S] {
  override val init: (S, Cmd[F, M]) = component.init
  override def update(
      state: S,
      value: Either[I, M]
  ): (S, Cmd[F, Either[O2, M]]) =
    val (state2, output) = component.update(state, value)
    val output2 = for {
      outOrMsg <- output
      transformed = outOrMsg.left.map(f)
    } yield transformed
    (state2, output2)
  override def view(state: S): Html[M] = component.view(state)
}