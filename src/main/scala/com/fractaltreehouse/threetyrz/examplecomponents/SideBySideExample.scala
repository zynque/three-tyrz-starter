package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*
import com.fractaltreehouse.threetyrz.examplecompositions.*

object SideBySideExample:
  val component =
    PairComponents(
      CounterButton.Component("Increment A"),
      CounterButton.Component("Increment B"),
      (a, b) => div(a.map(Left(_)), b.map(Right(_)))
    )

object SideBySideExample2:
  val b1 = CounterButton2.Component("Inc A")
  val b2 = CounterButton2.Component("Inc B")
  val paired = b1.pairWith(b2, (a, b) => div(a.map(Left(_)), b.map(Right(_))))
  val accumulated = paired.feedInto(EitherAccumulator())
  val withTotalAsString = accumulated.mapOutput {
    case (a, b) => (a.getOrElse(0) + b.getOrElse(0)).toString
  }
  val label = Label.Component("(total)")
  val component = withTotalAsString.feedInto(label, (a, b) => div(a.map(Left(_)), b.map(Right(_))))

class EitherAccumulator[F[_], A, B] extends TyrianComponent2[F, Either[A, B], (Option[A], Option[B]), Nothing, (Option[A], Option[B])] {
  def init: ((Option[A], Option[B]), Cmd[F, Nothing]) = ((None, None), Cmd.None)
  def update(state: (Option[A], Option[B]), value: Either[Either[A, B], Nothing]): ((Option[A], Option[B]), Cmd[F, Either[(Option[A], Option[B]), Nothing]]) =
    value match
      case Left(Left(a)) =>
        val newState = (Some(a), state._2)
        (newState, Cmd.emit(Left(newState)))
      case Left(Right(b)) =>
        val newState = (state._1, Some(b))
        (newState, Cmd.emit(Left(newState)))
      case _ => (state, Cmd.None)
        
  def view(state: (Option[A], Option[B])): Html[Nothing] = div()
}
