package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*
import com.fractaltreehouse.threetyrz.components.threejs.*
import com.fractaltreehouse.threetyrz.examplecompositions.*


enum AppMsg[+T]:
  case SwitchDirection
  case NoOp
  case ChildMessage(msg: T)

class DemoAppComponent
    extends ZIOTyrianComponent[
      ThreeJSDivComponentState[RotationDirection, SpinningCubeElements],
      AppMsg[ThreeJSDivMsg[RotationDirection, SpinningCubeElements] | UnexpectedError]
    ]:
  val threeDemo = new ThreeJSDivComponent(600, 600, SpinningCubeComposition)

  def init() =
    val (state, cmd) = threeDemo.init()
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
      div("Current Direction: " + state.model.toString),
      threeDemo.view(state).map(AppMsg.ChildMessage(_))
    )
