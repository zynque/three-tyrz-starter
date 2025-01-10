package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*

class DataComponentWithView[F[_], I, O, M, S](
    dataComponent: DataComponent[F, I, O, M, S],
    viewFunction: S => Html[M]
) extends TyrianComponent[F, I, O, M, S] {
  def init: (S, Cmd[F, M]) = dataComponent.init
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]]) =
    dataComponent.update(state, value)
  def view(state: S): Html[M] = viewFunction(state)
}
