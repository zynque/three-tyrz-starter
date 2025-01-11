package com.fractaltreehouse.threetyrz.examplecomponents

import com.fractaltreehouse.threetyrz.components.*
import tyrian.*
import tyrian.Html.*
import zio.*

object CounterButton:
  enum Msg:
    case Increment

  class Component(label: String)
      extends SimpleStatePropagatorComponent[Msg, Int]:

    def initSimple = 0

    def updateSimple(state: Int, message: Msg): Int =
      message match
        case Msg.Increment => state + 1

    def view(state: Int): Html[Msg] =
      div(
        button(onClick(Msg.Increment))(label),
        div(state.toString)
      )

object ButtonComponent:
  enum Msg:
    case Clicked

  class Component(label: String) extends ProducerComponent[Msg]:
    def view(state: Unit): Html[Msg] =
      button(onClick(Msg.Clicked))(label)
