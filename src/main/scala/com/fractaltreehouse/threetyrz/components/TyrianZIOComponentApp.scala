package com.fractaltreehouse.threetyrz.components

import tyrian.*
import tyrian.Html.*
import cats.effect.kernel.Async
import zio.*

trait TyrianZIOComponentApp[Msg, Model](
    component: ZIOTyrianComponent[Unit, Nothing, Model, Msg],
)(using Async[Task])
    extends TyrianZIOApp[Option[Msg], Model] {
  def router: Location => Option[Msg] = Routing.none(None)
  def init(flags: Map[String, String]): (Model, Cmd[Task, Option[Msg]]) =
    val (model, msg) = component.init(())
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
