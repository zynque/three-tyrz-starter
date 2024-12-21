package com.fractaltreehouse.threetyrz.components

import tyrian.*

trait TyrianComponent2[F[_], I, O, M, S] {
  def init: (S, Cmd[F, M])
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]])
  def view(state: S): Html[M]
}
