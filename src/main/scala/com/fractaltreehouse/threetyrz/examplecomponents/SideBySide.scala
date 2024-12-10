package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*
import com.fractaltreehouse.threetyrz.components.threejs.*
import com.fractaltreehouse.threetyrz.examplecompositions.*

object SideBySide:
  val component =
    PairComponents(
      IncrementButton.Component("Increment A"),
      IncrementButton.Component("Increment B"),
      (a, b) => div(a.map(Left(_)), b.map(Right(_)))
    )

object IncrementButton:
  enum Msg:
    case Increment
  
  class Component(label: String)
      extends ZIOTyrianComponent[Unit, Nothing, Int, Msg]:
      
    def init(i: Unit) = (0, Cmd.None)

    def update(state: Int, message: Msg): (Int, Cmd[Task, Msg]) =
      message match
        case Msg.Increment => (state + 1, Cmd.None)

    def view(state: Int): Html[Msg] =
      div(
        button(onClick(Msg.Increment))(label),
        div(state.toString)
      )

class PairComponents[AState, BState, AMsg, BMsg](
    componentA: ZIOTyrianComponent[Unit, Nothing, AState, AMsg],
    componentB: ZIOTyrianComponent[Unit, Nothing, BState, BMsg],
    combine: (Html[AMsg], Html[BMsg]) => Html[Either[AMsg, BMsg]]
) extends ZIOTyrianComponent[Unit, Nothing, (AState, BState), Either[AMsg, BMsg]]:
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
