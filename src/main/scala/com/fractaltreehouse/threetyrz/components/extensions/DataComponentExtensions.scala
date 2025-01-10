package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*

object DataComponentExtensions {
  extension [F[_], I, O, M, S](dataComponent: DataComponent[F, I, O, M, S]) {
    def withView(
        view: S => Html[M]
    ): TyrianComponent[F, I, O, M, S] =
      new DataComponentWithView(dataComponent, view)
  }
}
