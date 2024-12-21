package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*
import com.fractaltreehouse.threetyrz.examplecompositions.*

class PipeComponents[AState, BState, Msg](
    componentA: ZIOTyrianComponent[AState, Msg],
    componentB: ZIOTyrianComponent[BState, Msg],
    combine: (Html[Msg], Html[Msg]) => Html[Msg]
) extends ZIOTyrianComponent[
      (AState, BState),
      Msg
    ]:
  def init() =
    val (aState, aCmd) = componentA.init()
    val (bState, bCmd) = componentB.init()
    ((aState, bState), aCmd |+| bCmd) // todo: what if componentb init depends on componenta init?

  def update(
      state: (AState, BState),
      message: Msg
  ): ((AState, BState), Cmd[Task, Msg]) =
    val (newAState, aCmd) = componentA.update(state._1, message)
    val (newBState, bCmd) = componentB.update(state._2, message)
    ((newAState, newBState), aCmd |+| bCmd)

  def view(
      state: (AState, BState)
  ): Html[Msg] =
    combine(componentA.view(state._1), componentB.view(state._2))
