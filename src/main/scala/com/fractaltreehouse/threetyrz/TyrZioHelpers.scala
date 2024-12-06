package com.fractaltreehouse.threetyrz

import tyrian.*
import zio.*
import zio.interop.catz.*

case class UnexpectedError(msg: String)

extension [Msg] (task: Task[Msg | UnexpectedError])
  def reportFailures: Task[Msg | UnexpectedError] =
    task.catchAll(e => ZIO.succeed(UnexpectedError(e.toString)))

extension [Msg] (task: Task[Msg | UnexpectedError])
  def toCommand: Cmd[Task, Msg | UnexpectedError] = Cmd.Run(task.reportFailures)
