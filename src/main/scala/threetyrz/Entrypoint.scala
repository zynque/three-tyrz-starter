package threetyrz

import tyrian.Html.*
import tyrian.*
import zio.*
import zio.interop.catz.*
import scala.scalajs.js.annotation.*

type Model = RotationDirection

enum RotationDirection:
  case Left
  case Right

enum Msg:
  case SwitchDirection
  case NoOp

@JSExportTopLevel("ThreeTyrz")
object ThreeTyrz extends TyrianZIOApp[Msg, Model]:

  def router: Location => Msg =
    Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[Task, Msg]) =
    (RotationDirection.Right, Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[Task, Msg]) =
    case Msg.SwitchDirection =>
      model match
        case RotationDirection.Left  => (RotationDirection.Right, Cmd.None)
        case RotationDirection.Right => (RotationDirection.Left, Cmd.None)
    case Msg.NoOp =>
      (model, Cmd.None)

  def view(model: Model): Html[Msg] =
    div(
      button(onClick(Msg.SwitchDirection))("Toggle Direction"),
      div(model.toString)
    )

  def subscriptions(model: Model): Sub[Task, Msg] =
    Sub.None

