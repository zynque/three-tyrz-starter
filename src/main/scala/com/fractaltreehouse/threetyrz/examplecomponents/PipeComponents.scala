package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*
import com.fractaltreehouse.threetyrz.examplecompositions.*

class PipeComponents[AState, BState, Msg](
    componentA: ZIOTyrianComponent[Unit, Nothing, AState, Msg],
    componentB: ZIOTyrianComponent[Unit, Nothing, BState, Msg],
    combine: (Html[Msg], Html[Msg]) => Html[Msg]
) extends ZIOTyrianComponent[
      Unit,
      Nothing,
      (AState, BState),
      Msg
    ]:
  def init(i: Unit) =
    val (aState, aCmd) = componentA.init(i)
    val (bState, bCmd) = componentB.init(i)
    ((aState, bState), aCmd |+| bCmd) // todo: what if componentb init depends on componenta init?

  def update(
      state: (AState, BState),
      message: Msg
  ): ((AState, BState), Cmd[Task, Msg]) =
    ???
    // val (newAState, aCmd) = componentA.update(state._1, message)
    // val (newBState, bCmd) = componentB.update(state._2, bMsg)
    // ((state._1, newBState), bCmd)

  def view(
      state: (AState, BState)
  ): Html[Msg] =
    combine(componentA.view(state._1), componentB.view(state._2))
