package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*
import com.fractaltreehouse.threetyrz.examplecompositions.*

def SideBySideExample = {
  val b1     = CounterButton.Component("Inc A")
  val b2     = CounterButton.Component("Inc B")
  val paired = b1.pairWith(b2, (a, b) => div(a.map(Left(_)), b.map(Right(_))))
  val accumulated = paired.eitherAccumulated
  val withTotalAsString = accumulated.mapOutput { case (a, b) =>
    (a.getOrElse(0) + b.getOrElse(0)).toString
  }
  val label = Label.Component("(total)")
  withTotalAsString.feedInto(
    label,
    (a, b) => div(a.map(Left(_)), b.map(Right(_)))
  )
}
