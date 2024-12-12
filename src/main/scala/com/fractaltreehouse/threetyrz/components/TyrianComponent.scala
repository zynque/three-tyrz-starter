package com.fractaltreehouse.threetyrz.components

import tyrian.*
import tyrian.Html.*
import cats.effect.kernel.Async
import zio.*

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
