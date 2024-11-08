package com.fractaltreehouse.threetyrz

import tyrian.Html.*
import tyrian.*
import tyrian.CSS.*
import zio.*
import zio.interop.catz.*
import scala.scalajs.js.annotation.*
import scala.concurrent.duration.*

type Model = RotationDirection

enum RotationDirection:
  case Left
  case Right

enum Msg:
  case SwitchDirection
  case NoOp
  case InitThreeJS
  case RetryInitThreeJS
  case InternalError(message: String)

// extension on Task[Msg] to handle failures and put them in the message
extension (task: Task[Msg])
  def reportFailures: Task[Msg] =
    task.catchAll(e => ZIO.succeed(Msg.InternalError(e.toString)))

@JSExportTopLevel("ThreeTyrz")
object ThreeTyrz extends TyrianZIOApp[Msg, Model]:

  def router: Location => Msg =
    Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[Task, Msg]) =
    (RotationDirection.Right, Cmd.Emit(Msg.InitThreeJS))

  def update(model: Model): Msg => (Model, Cmd[Task, Msg]) =
    case Msg.SwitchDirection =>
      model match
        case RotationDirection.Left  => (RotationDirection.Right, Cmd.None)
        case RotationDirection.Right => (RotationDirection.Left, Cmd.None)
    case Msg.NoOp =>
      (model, Cmd.None)
    case Msg.InitThreeJS => 
      (model, Cmd.Run(threeDemoTask.reportFailures))
    case Msg.RetryInitThreeJS => 
      (model, Cmd.emitAfterDelay(Msg.InitThreeJS, 0.5.seconds))
    case Msg.InternalError(message) =>
      println(s"Internal error: $message") // todo: purify
      (model, Cmd.None)

  def view(model: Model): Html[Msg] =
    div(
      button(onClick(Msg.SwitchDirection))("Toggle Direction"),
      div(model.toString),
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
