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
  val accumulated = paired.eitherAccumulated
  val withTotalAsString = accumulated.mapOutput {
    case (a, b) => (a.getOrElse(0) + b.getOrElse(0)).toString
  }
  val label = Label.Component("(total)")
  val component = withTotalAsString.feedInto(label, (a, b) => div(a.map(Left(_)), b.map(Right(_))))
