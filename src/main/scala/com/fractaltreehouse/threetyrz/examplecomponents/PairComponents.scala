package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*
import com.fractaltreehouse.threetyrz.examplecompositions.*

class PairComponents[AState, BState, AMsg, BMsg](
    componentA: ZIOTyrianComponent[Unit, Nothing, AState, AMsg],
    componentB: ZIOTyrianComponent[Unit, Nothing, BState, BMsg],
    combine: (Html[AMsg], Html[BMsg]) => Html[Either[AMsg, BMsg]]
) extends ZIOTyrianComponent[
      Unit,
      Nothing,
      (AState, BState),
      Either[AMsg, BMsg]
    ]:
  def init(i: Unit) =
    val (aState, aCmd) = componentA.init(i)
    val (bState, bCmd) = componentB.init(i)
    ((aState, bState), aCmd.map(Left(_)) |+| bCmd.map(Right(_)))

  def update(
      state: (AState, BState),
      message: Either[AMsg, BMsg]
  ): ((AState, BState), Cmd[Task, Either[AMsg, BMsg]]) =
    message match
      case Left(aMsg) =>
        val (newAState, aCmd) = componentA.update(state._1, aMsg)
        ((newAState, state._2), aCmd.map(Left(_)))
      case Right(bMsg) =>
        val (newBState, bCmd) = componentB.update(state._2, bMsg)
        ((state._1, newBState), bCmd.map(Right(_)))

  def view(
      state: (AState, BState)
  ): Html[Either[AMsg, BMsg]] =
    combine(componentA.view(state._1), componentB.view(state._2))
