package com.fractaltreehouse.threetyrz

import tyrian.Html.*
import tyrian.*
import tyrian.CSS.*
import zio.*
import zio.interop.catz.*
import scala.scalajs.js.annotation.*
import com.fractaltreehouse.threejs.interop.EulerAngle

case class Model(currentDirection: RotationDirection, sceneData: Option[SceneData])

enum RotationDirection:
  case Left
  case Right

enum Msg:
  case SwitchDirection
  case NoOp
  case InitThreeJS
  case RetryInitThreeJS
  case InternalError(message: String)
  case ClockTick
  case RenderScene(sceneData: SceneData)
  case WaitForNextClockTick
  case SceneInitialized(sceneData: SceneData)

// extension on Task[Msg] to handle failures and put them in the message
extension (task: Task[Msg])
  def reportFailures: Task[Msg] =
    task.catchAll(e => ZIO.succeed(Msg.InternalError(e.toString)))

@JSExportTopLevel("ThreeTyrz")
object ThreeTyrz extends TyrianZIOApp[Msg, Model]:

  def router: Location => Msg =
    Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[Task, Msg]) =
    val initialModel = Model(RotationDirection.Left, None)
    (initialModel, Cmd.Emit(Msg.InitThreeJS))

  def update(model: Model): Msg => (Model, Cmd[Task, Msg]) =
    case Msg.SwitchDirection =>
      model match
        case Model(RotationDirection.Left, sceneData)  =>
          (Model(RotationDirection.Right, sceneData), Cmd.None)
        case Model(RotationDirection.Right, sceneData) =>
          (Model(RotationDirection.Left, sceneData), Cmd.None)
    case Msg.NoOp =>
      (model, Cmd.None)
    case Msg.InitThreeJS => 
      (model, threeDemoTask.reportFailures.toCommand)
    case Msg.RetryInitThreeJS => 
      (model, ZIO.succeed(Msg.InitThreeJS).delay(100.millis).toCommand)
    case Msg.InternalError(message) =>
      (model, Cmd.None)
    case Msg.ClockTick =>
      (model, updateScene(model).reportFailures.toCommand)
    case Msg.RenderScene(sceneData) =>
      (model.copy(sceneData = Some(sceneData)), renderScene(sceneData).toCommand)
    case Msg.WaitForNextClockTick =>
      (model, ZIO.succeed(Msg.ClockTick).delay(10.millis).toCommand)
    case Msg.SceneInitialized(sceneData) =>
      (model.copy(sceneData = Some(sceneData)), renderScene(sceneData).toCommand)

  def view(model: Model): Html[Msg] =
    div(
      button(onClick(Msg.SwitchDirection))("Toggle Direction"),
      div(model.currentDirection.toString),
      div("THREE:"),
      div(id := "three-container")(),
      div(":THREE")
    )

  // todo
  // val resizes: Sub[Task, Msg] = 
  //   Sub.fromEvent("resize", org.scalajs.dom.window) {
  //     case _ => Msg.Resized
  //   }

  def subscriptions(model: Model): Sub[Task, Msg] =
    Sub.None

  extension (task: Task[Msg])
    def toCommand: Cmd[Task, Msg] = Cmd.Run(task.reportFailures)
