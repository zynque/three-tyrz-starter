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

// I: Input, O: Output, S: State/Model, M: Message
trait TyrianComponent[F[_], I, O, S, M] {
  val children: Vector[TyrianComponent[F, Any, Any, Any, Any]] = Vector.empty
  def init(input: I): (S, Cmd[F, M])
  def update(state: S, message: M): (S, Cmd[F, M])
  def view(state: S): Html[M]
  def addChild[CI, CO, CS, CM](child: TyrianComponent[F, CI, CO, CS, CM]) = {
    children :+ child
  }
}

trait ZIOTyrianComponent[I, O, S, M] extends TyrianComponent[Task, I, O, S, M]

// def foo[Msg, Model](c: TyrianComponent[Msg, Model]) = {
//   // good html[msg] has map, which we will need to put inner components message inside outer component's message
//   def v2(m: Model) = c.view(m).map(minner => mouter)
// }
