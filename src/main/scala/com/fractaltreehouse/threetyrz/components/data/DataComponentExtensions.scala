package com.fractaltreehouse.threetyrz.components.data

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*
export DataComponentExtensions.*

object DataComponentExtensions {
  extension [F[_], I, O, M, S](component: DataComponent[F, I, O, M, S]) {
    def withView(
        view: S => Html[M]
    ): TyrianComponent[F, I, O, M, S] =
      new DataComponentWithView(component, view)

    def feedInto[O2, M2, S2](
        component2: DataComponent[F, O, O2, M2, S2]
    ): DataComponent[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] =
      new DataFedInto(component, component2)

    def mapOutput[O2](f: O => O2): DataComponent[F, I, O2, M, S] =
      new OutputMapped(component, f)

    def pairWith[I2, O2, M2, S2](
        component2: DataComponent[F, I2, O2, M2, S2]
    ): DataComponent[F, Either[I, I2], Either[O, O2], Either[M, M2], (S, S2)] =
      new PairedWith(component, component2)

    def propagateState: DataComponent[F, I, S, M, S] =
      new StatePropagated(component)
  }
}
