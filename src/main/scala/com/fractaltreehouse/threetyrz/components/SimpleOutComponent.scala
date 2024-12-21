package com.fractaltreehouse.threetyrz.components

import tyrian.*
import zio.*

trait SimpleOutComponent[M, S] extends TyrianComponent2[Task, Nothing, S, M, S]:
  def updateSimple(state: S, message: M): (S, Cmd[Task, M])
  def update(state: S, value: Either[Nothing, M]): (S, Cmd[Task, Either[Nothing, M]]) =
    val (s, c) = value match
      case Right(m) => updateSimple(state, m)
      case Left(_) => (state, Cmd.None)
    (s, c.map(Right(_)))
