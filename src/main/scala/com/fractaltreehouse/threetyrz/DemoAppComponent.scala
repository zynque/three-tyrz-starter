package com.fractaltreehouse.threetyrz

import tyrian.*
import tyrian.Html.*
import zio.*

enum AppMsg[+T]:
  case SwitchDirection
  case NoOp
  case ChildMessage(msg: T)

enum RotationDirection:
  case Left
  case Right

class DemoAppComponent
    extends ZIOTyrianComponent[
      Unit,
      Nothing,
      ThreeJSDivComponentState[RotationDirection, SpinningCubeElements],
      AppMsg[ThreeJSDivMsg[RotationDirection, SpinningCubeElements] | UnexpectedError]
    ]:
  val threeDemo = new ThreeJSDivComponent(600, 600, SpinningCubeComposition)

  def init(i: Unit) =
    val (state, cmd) = threeDemo.init(i)
    (state, cmd.map(AppMsg.ChildMessage(_)))
  def update(
      state: ThreeJSDivComponentState[RotationDirection, SpinningCubeElements],
      message: AppMsg[ThreeJSDivMsg[RotationDirection, SpinningCubeElements] | UnexpectedError]
  ): (
      ThreeJSDivComponentState[RotationDirection, SpinningCubeElements],
      Cmd[Task, AppMsg[ThreeJSDivMsg[RotationDirection, SpinningCubeElements] | UnexpectedError]]
  ) =
    message match {
      case AppMsg.SwitchDirection =>
        state.model match
          case RotationDirection.Left =>
            (state.copy(model = RotationDirection.Right), Cmd.None)
          case RotationDirection.Right =>
            (state.copy(model = RotationDirection.Left), Cmd.None)
      case AppMsg.NoOp =>
        (state, Cmd.None)
      case AppMsg.ChildMessage(msg) =>
        val (newState, cmd) = threeDemo.update(state, msg)
        (newState, cmd.map(AppMsg.ChildMessage(_)))
    }
  def view(
      state: ThreeJSDivComponentState[RotationDirection, SpinningCubeElements]
  ): Html[AppMsg[ThreeJSDivMsg[RotationDirection, SpinningCubeElements] | UnexpectedError]] =
    div(
      button(onClick(AppMsg.SwitchDirection))("Toggle Direction"),
      div("Current Direction: " + state.toString),
      threeDemo.view(state).map(AppMsg.ChildMessage(_))
    )
