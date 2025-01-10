package com.fractaltreehouse.threetyrz.components.data

import tyrian.*

trait DataComponent[F[_], I, O, M, S]:
  def init: (S, Cmd[F, M])
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]])
  