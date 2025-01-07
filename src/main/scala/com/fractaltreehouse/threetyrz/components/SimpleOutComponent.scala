package com.fractaltreehouse.threetyrz.components

import tyrian.*
import zio.*

trait ProducerComponent[O, S] extends TyrianComponent[Task, Nothing, S, O, S]:
  def updateSimple(state: S): (S, Cmd[Task, O])
  def update(state: S, value: Either[Nothing, O]): (S, Cmd[Task, Either[Nothing, O]]) =
    val (s, c) = value match
      case Right(_) => updateSimple(state)
      case Left(_) => (state, Cmd.None)
    (s, c.map(Right(_)))

trait SimpleStatePropagatorComponent[M, S] extends TyrianComponent[Task, Nothing, S, M, S]:
  def initSimple: S
  def updateSimple(state: S, message: M): S
  def init: (S, Cmd[Task, M]) = (initSimple, Cmd.None)
  def update(state: S, value: Either[Nothing, M]): (S, Cmd[Task, Either[S, M]]) =
    value match
      case Left(_) => (state, Cmd.None)
      case Right(m) =>
        val newState = updateSimple(state, m)
        (newState, Cmd.emit(Left(newState)))

trait SimpleConsumerComponent[I, S] extends TyrianComponent[Task, I, Nothing, Nothing, S]:
  def initSimple: S
  def updateSimple(state: S, message: I): S

  def init: (S, Cmd[Task, Nothing]) = (initSimple, Cmd.None)
  def update(state: S, value: Either[I, Nothing]): (S, Cmd[Task, Either[Nothing, Nothing]]) =
    value match
      case Left(m) =>
        val newState = updateSimple(state, m)
        (newState, Cmd.None)
      case Right(_) => (state, Cmd.None)
