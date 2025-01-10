package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*
import com.fractaltreehouse.threetyrz.components.data.DataComponentExtensions.withView
import com.fractaltreehouse.threetyrz.components.data.CompositionMsg
import com.fractaltreehouse.threetyrz.components.data.EitherAccumulator
import com.fractaltreehouse.threetyrz.components.data.OutputMapped
import com.fractaltreehouse.threetyrz.components.data.PairedWith
import com.fractaltreehouse.threetyrz.components.data.DataComponent

object TyrianComponentExtensions {
  extension [F[_], I, O, M, S](component: TyrianComponent[F, I, O, M, S]) {
    def feedInto[O2, M2, S2](
        component2: TyrianComponent[F, O, O2, M2, S2],
        combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
    ): TyrianComponent[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] =
      new FedInto[F, I, O, M, S, O2, M2, S2](component, component2, combineUI)

    def feedInto[O2, M2, S2](
        component2: DataComponent[F, O, O2, M2, S2]
    ): TyrianComponent[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] =
      new FedIntoDataComponent[F, I, O, M, S, O2, M2, S2](component, component2)

    def mapOutput[O2](f: O => O2): TyrianComponent[F, I, O2, M, S] =
      new OutputMapped[F, I, O, M, S, O2](component, f).withView(component.view)

    def pairWith[I2, O2, M2, S2](
        component2: TyrianComponent[F, I2, O2, M2, S2],
        combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
    ): TyrianComponent[F, Either[I, I2], Either[O, O2], Either[
      M,
      M2
    ], (S, S2)] =
      new PairedWith[F, I, O, M, S, I2, O2, M2, S2](
        component,
        component2
      ).withView((s, s2) => combineUI(component.view(s), component2.view(s2)))

    def propagateState: TyrianComponent[F, I, S, M, S] =
      new StatePropagated[F, I, O, M, S](component)
  }

  extension [F[_], I, OA, OB, M, S](component: TyrianComponent[F, I, Either[OA, OB], M, S]) {
    def eitherAccumulated = component.feedInto(new EitherAccumulator[F, OA, OB])
  }
}
