package com.fractaltreehouse.threetyrz.components

import tyrian.*
import zio.*

// todo: generalize effect
// todo: move

trait ProducerComponent[O] extends TyrianComponent[Task, Nothing, O, O, Unit]:
  def init: (Unit, Cmd[Task, O]) = ((), Cmd.None)
  def update(state: Unit, value: Either[Nothing, O]): (Unit, Cmd[Task, Either[O, O]]) =
    value match
      case Right(msg) => (state, Cmd.emit(Left(msg)))
      case _ => (state, Cmd.None)

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
