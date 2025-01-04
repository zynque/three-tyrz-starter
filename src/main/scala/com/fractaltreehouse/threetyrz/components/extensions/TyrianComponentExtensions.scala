package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*

object TyrianComponentExtensions {
  extension [F[_], I, O, M, S](component: TyrianComponent2[F, I, O, M, S]) {
    def feedInto[O2, M2, S2](
        component2: TyrianComponent2[F, O, O2, M2, S2],
        combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
    ): TyrianComponent2[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] =
      new FedInto[F, I, O, M, S, O2, M2, S2](component, component2, combineUI)

    // does not display component2 - use for downstream components that do stateful data transformations but have no ui
    def feedInto[O2, M2, S2](
        component2: TyrianComponent2[F, O, O2, M2, S2]
    ): TyrianComponent2[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] =
      feedInto(component2, (h1, h2) => h1.map(Left(_)))

    def mapOutput[O2](f: O => O2): TyrianComponent2[F, I, O2, M, S] =
      new OutputMapped[F, I, O, M, S, O2](component, f)

    def pairWith[I2, O2, M2, S2](
        component2: TyrianComponent2[F, I2, O2, M2, S2],
        combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
    ): TyrianComponent2[F, Either[I, I2], Either[O, O2], Either[
      M,
      M2
    ], (S, S2)] =
      new PairedWith[F, I, O, M, S, I2, O2, M2, S2](
        component,
        component2,
        combineUI
      )

    def propagateState: TyrianComponent2[F, I, S, M, S] =
      new StatePropagated[F, I, O, M, S](component)
  }

  extension [F[_], I, OA, OB, M, S](component: TyrianComponent2[F, I, Either[OA, OB], M, S]) {
    def eitherAccumulated = component.feedInto(new EitherAccumulator[F, OA, OB])
  }
}
