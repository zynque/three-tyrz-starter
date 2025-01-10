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

// toggle example
// three buttons, each, when pressed, reveals a different counter on a shared div

def ToggleExample = {
  val incA  = CounterButton.Component("Inc A")
  val incB  = CounterButton.Component("Inc B")

  val showA = ButtonComponent.Component("Show A")
  val showB = ButtonComponent.Component("Show B")
  val selector = showA.pairWith(showB, (a, b) => div(a.map(Left(_)), b.map(Right(_))))
  
  // todo: need to be able to split output?
  
  val message = selector.mapOutput{
    case Left(_) => "A Selected"
    case Right(_) => "B Selected"
  }
  val messageDisplayed = message.feedInto(
    Label.Component(". . ."),
    (a, b) => div(a.map(Left(_)), b.map(Right(_)))
  )

  // val displaySelected =
  //   Switcher(
  //     selector,
  //     incA,
  //     incB,
  //     (a, b) => div(a.map(Left(_)), b.map(Right(_)))
  //   )

  messageDisplayed
}

def DynamicSubcomponentCreationExample = {
  // we want to be able to create a new subcomponent at run time, based on user-supplied parameters
  // and responsively display it


}

def ListExample = {}
