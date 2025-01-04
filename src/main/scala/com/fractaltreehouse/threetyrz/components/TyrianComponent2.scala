package com.fractaltreehouse.threetyrz.components

import tyrian.*
export com.fractaltreehouse.threetyrz.components.extensions.TyrianComponentExtensions.*

trait TyrianComponent2[F[_], I, O, M, S] {
  def init: (S, Cmd[F, M])
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]])
  def view(state: S): Html[M]
}

// todo: read and re-read this: https://dev.to/dwayne/stateless-and-stateful-components-no-reusable-views-in-elm-2kg0
//       compare with what we're trying to do here
//
//       also compare/contrast using purely functional streams as in/out ports
