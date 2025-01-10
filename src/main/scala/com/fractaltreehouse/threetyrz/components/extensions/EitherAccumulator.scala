package com.fractaltreehouse.threetyrz.components.extensions

import tyrian.*
import tyrian.Html.*
import com.fractaltreehouse.threetyrz.components.*

// consumes either A or B, and stores the most recent value of each, or None if no value has been received
class EitherAccumulator[F[_], A, B]
    extends DataComponent[
      F,
      Either[A, B],
      (Option[A], Option[B]),
      Nothing,
      (Option[A], Option[B])
    ] {
  def init: ((Option[A], Option[B]), Cmd[F, Nothing]) = ((None, None), Cmd.None)
  def update(
      state: (Option[A], Option[B]),
      value: Either[Either[A, B], Nothing]
  ): ((Option[A], Option[B]), Cmd[F, Either[(Option[A], Option[B]), Nothing]]) =
    val (sa, sb) = state
    value match
      case Left(Left(a)) =>
        val newState = (Some(a), sb)
        (newState, Cmd.emit(Left(newState)))
      case Left(Right(b)) =>
        val newState = (sa, Some(b))
        (newState, Cmd.emit(Left(newState)))
      case _ => (state, Cmd.None)
}
