package com.fractaltreehouse.threetyrz.components.data

import tyrian.*

// A Tyrian Component that handles messages and state but has no view
// F: Effect Type (Cats Effect or ZIO Task)
// I: Input, O: Output, S: State/Model, M: Message
trait DataComponent[F[_], I, O, M, S]:
  def init: (S, Cmd[F, M])
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]])
