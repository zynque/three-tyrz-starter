package com.fractaltreehouse.threetyrz.components

import tyrian.*
import tyrian.Html.*
import cats.effect.kernel.Async
import zio.*

trait TyrianZIOComponentApp[Msg, Model](
    component: ZIOTyrianComponent[Model, Msg],
)(using Async[Task])
    extends TyrianZIOApp[Option[Msg], Model] {
  def router: Location => Option[Msg] = Routing.none(None)
  def init(flags: Map[String, String]): (Model, Cmd[Task, Option[Msg]]) =
    val (model, msg) = component.init()
    (model, msg.map(Some(_)))
  def update(model: Model): Option[Msg] => (Model, Cmd[Task, Option[Msg]]) =
    case Some(msg) =>
      val (model2, cmd) = component.update(model, msg)
      (model2, cmd.map(Some(_)))
    case None => (model, Cmd.None)
  def view(model: Model): Html[Option[Msg]]               =
     component.view(model).map(Some(_))
  def subscriptions(model: Model): Sub[Task, Option[Msg]] = Sub.None
}

trait TyrianZIOComponentApp2[I, O, M, S](
  component: TyrianComponent2[Task, I, O, M, S]
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
      (model, Cmd.None)
  }

  def view(model: S): Html[ComponentMessage[I, O, M] | UnexpectedError] = component.view(model).map(ComponentMessage.InternalMessage(_))
}
