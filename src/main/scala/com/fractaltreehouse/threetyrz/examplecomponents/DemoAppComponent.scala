package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*
import com.fractaltreehouse.threetyrz.components.threejs.*
import com.fractaltreehouse.threetyrz.examplecompositions.*

def DemoAppComponent = SpinningCubeDemoApp.pairWith(
  SideBySideExample,
  (h1, h2) => div(h1.map(Left(_)), h2.map(Right(_)))
)

def SpinningCubeDemoApp = DirectionSwitcher().feedInto(
  ThreeJSDivComponent(600, 600, SpinningCubeComposition),
  (h1, h2) => div(h1.map(Left(_)), h2.map(Right(_)))
)

enum AppMsg:
  case SwitchDirection
  case NoOp

class DirectionSwitcher
    extends TyrianComponent[
      Task,
      Any,
      RotationDirection,
      AppMsg,
      RotationDirection
    ]:

  def init = (RotationDirection.Left, Cmd.None)

  def update(
      state: RotationDirection,
      value: Either[Any, AppMsg]
  ): (
      RotationDirection,
      Cmd[Task, Either[RotationDirection, AppMsg]]
  ) =
    value match {
      case Right(AppMsg.SwitchDirection) =>
        state match
          case RotationDirection.Left =>
            (RotationDirection.Right, Cmd.emit(Left(RotationDirection.Right)))
          case RotationDirection.Right =>
            (RotationDirection.Left, Cmd.emit(Left(RotationDirection.Left)))
      case _ =>
        (state, Cmd.None)
    }

  def view(state: RotationDirection): Html[AppMsg] =
    div(
      button(onClick(AppMsg.SwitchDirection))("Toggle Direction"),
      div("Current Direction: " + state.toString)
    )
