package com.fractaltreehouse.threetyrz.components.data

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*

// Apply a function to transform the input channel before passing it to the component
class InputContramapped[F[_], I, I2, O, M, S](
    component: DataComponent[F, I, O, M, S],
    f: I2 => I
) extends DataComponent[F, I2, O, M, S] {
  override val init: (S, Cmd[F, M]) = component.init
  override def update(
      state: S,
      value: Either[I2, M]
  ): (S, Cmd[F, Either[O, M]]) =
    value match {
      case Left(i2) =>
        val i = f(i2)
        component.update(state, Left(i))
      case Right(m) =>
        component.update(state, Right(m))
    }
  }
