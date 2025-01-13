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

// When input == true, displays first counter, when input == false, displays second counter
class Toggle
    extends TyrianComponent[
      Task,
      Boolean,
      Nothing,
      Boolean,
      (Boolean, Int, Int)
    ] {
  val incA = CounterButton.Component("Inc A").propagateState
  val incB = CounterButton.Component("Inc B").propagateState

  def init: ((Boolean, Int, Int), Cmd[Task, Boolean]) = ((true, 0, 0), Cmd.None)
  def update(
      state: (Boolean, Int, Int),
      value: Either[Boolean, Boolean]
  ): ((Boolean, Int, Int), Cmd[Task, Either[Nothing, Boolean]]) = {
    val (b, i1, i2) = state
    value match {
      case Left(b)      => ((b, i1, i2), Cmd.None)
      case Right(true)  => ((b, i1 + 1, i2), Cmd.None)
      case Right(false) => ((b, i1, i2 + 1), Cmd.None)
    }
  }
  def view(state: (Boolean, Int, Int)): Html[Boolean] = {
    val (b, i1, i2) = state
    if b then incA.view(i1).map(_ => true)
    else incB.view(i2).map(_ => false)
  }
}

// toggle example
// two buttons, each, when pressed, reveals a different counter instance
def ToggleExample = {
  val incA = CounterButton.Component("Inc A")
  val incB = CounterButton.Component("Inc B")

  val showA = ButtonComponent.Component("Show A")
  val showB = ButtonComponent.Component("Show B")
  val selector = showA
    .pairWith(showB, (a, b) => div(a.map(Left(_)), b.map(Right(_))))
    .mapOutput {
      case Left(_)  => true
      case Right(_) => false
    }
  val toggle = Toggle()

  selector.feedInto(toggle, (a, b) => div(a.map(Left(_)), b.map(Right(_))))

  // todo: need to be able to split output?

}

def DynamicSubcomponentCreationExample = {
  // we want to be able to create a new subcomponent at run time, based on user-supplied parameters
  // and responsively display it

}

def ListExample = {}
