package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*
import com.fractaltreehouse.threetyrz.components.data.CompositionMsg
import com.fractaltreehouse.threetyrz.components.data.DataFedInto

class FedInto[F[_], I, O, M, S, O2, M2, S2](
    component: TyrianComponent[F, I, O, M, S],
    component2: TyrianComponent[F, O, O2, M2, S2],
    combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
) extends DataFedInto[F, I, O, M, S, O2, M2, S2](
      component,
      component2
    )
    with TyrianComponent[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] {
  def view(state: (S, S2)): Html[CompositionMsg[O, M, M2]] = {
    val (s, s2) = state
    val h1      = component.view(s)
    val h2      = component2.view(s2)
    combineUI(h1, h2).map {
      case Left(m)   => CompositionMsg.Msg1(m)
      case Right(m2) => CompositionMsg.Msg2(m2)
    }
  }
}
