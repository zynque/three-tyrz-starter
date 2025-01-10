package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*

class FedIntoDataComponent[F[_], I, O, M, S, O2, M2, S2](
    component: TyrianComponent[F, I, O, M, S],
    component2: DataComponent[F, O, O2, M2, S2]
) extends DataFedInto[F, I, O, M, S, O2, M2, S2](
      component,
      component2
    )
    with TyrianComponent[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] {
  def view(state: (S, S2)): Html[CompositionMsg[O, M, M2]] = {
    val (s, s2) = state
    val h       = component.view(s)
    h.map(m => CompositionMsg.Msg1(m))
  }
}
