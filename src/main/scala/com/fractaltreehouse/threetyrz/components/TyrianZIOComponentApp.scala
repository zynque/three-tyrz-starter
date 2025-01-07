package com.fractaltreehouse.threetyrz.components

import tyrian.*
import tyrian.Html.*
import cats.effect.kernel.Async
import zio.*

trait TyrianZIOComponentApp[I, O, M, S](
  component: TyrianComponent[Task, I, O, M, S]
)(using Async[Task])
  extends TyrianZIOApp[ComponentMessage[I, O, M] | UnexpectedError, S] {

  def subscriptions(model: S): Sub[Task, ComponentMessage[I, O, M] | UnexpectedError] = Sub.None
  def router: Location => ComponentMessage[I, O, M] | UnexpectedError = Routing.none(ComponentMessage.None)
  def init(flags: Map[String, String]): (S, Cmd[Task, ComponentMessage[I, O, M] | UnexpectedError]) = {
    val (model, cmd) = component.init
    val cmd2 = for {
      m <- cmd
      componentMessage = ComponentMessage.InternalMessage(m)
    } yield componentMessage
    (model, cmd2)
  }
  def update(model: S): ComponentMessage[I, O, M] | UnexpectedError => (S, Cmd[Task, ComponentMessage[I, O, M] | UnexpectedError]) = {
    case ComponentMessage.InternalMessage(msg) =>
      val (model2, cmd) = component.update(model, Right(msg))
      val cmd2 = for {
        outOrMsg <- cmd
        componentMessage = outOrMsg match {
          case Left(out) => ComponentMessage.Output(out)
          case Right(msg) => ComponentMessage.InternalMessage(msg)
        }
      } yield componentMessage
      (model2, cmd2)
    case ComponentMessage.Input(msg) =>
      val (model2, cmd) = component.update(model, Left(msg))
      val cmd2 = for {
        outOrMsg <- cmd
        componentMessage = outOrMsg match {
          case Left(out) => ComponentMessage.Output(out)
          case Right(msg) => ComponentMessage.InternalMessage(msg)
        }
      } yield componentMessage
      (model2, cmd2)
    case ComponentMessage.Output(out) =>
      (model, Cmd.None)
    case ComponentMessage.None =>
      (model, Cmd.None)
    case UnexpectedError(msg) =>
      // todo: log error
      (model, Cmd.None)
  }

  def view(model: S): Html[ComponentMessage[I, O, M] | UnexpectedError] = component.view(model).map(ComponentMessage.InternalMessage(_))
}
