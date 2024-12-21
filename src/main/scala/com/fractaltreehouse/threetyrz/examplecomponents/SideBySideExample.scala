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
  val component =
    CounterButton2.Component("Inc A").pairWith(
      CounterButton2.Component("Inc B"),
      (a, b) => div(a.map(Left(_)), b.map(Right(_)))
    )
