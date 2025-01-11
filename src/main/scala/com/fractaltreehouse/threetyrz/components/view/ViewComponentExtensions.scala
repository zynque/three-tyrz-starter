package com.fractaltreehouse.threetyrz.components.view

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*
export ViewComponentExtensions.*

object ViewComponentExtensions {
  extension [M, S](component: ViewComponent[M, S]) {
    def asComponent[F[_], I, O](
        initialState: S
    ): TyrianComponent[F, I, O, M, S] =
      new ViewOnlyComponent(component, initialState)
  }
}
