package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*

object CounterButton:
  enum Msg:
    case Increment
  
  class Component(label: String)
      extends ZIOTyrianComponent[Int, Msg]:
      
    def init() = (0, Cmd.None)

    def update(state: Int, message: Msg): (Int, Cmd[Task, Msg]) =
      message match
        case Msg.Increment => (state + 1, Cmd.None)

    def view(state: Int): Html[Msg] =
      div(
        button(onClick(Msg.Increment))(label),
        div(state.toString)
      )

object CounterButton2:
  enum Msg:
    case Increment
  
  class Component(label: String)
      extends SimpleOutComponent[Msg, Int]:
      
    def init = (0, Cmd.None)

    def updateSimple(state: Int, message: Msg): (Int, Cmd[Task, Msg]) =
      message match
        case Msg.Increment => (state + 1, Cmd.None)

    def view(state: Int): Html[Msg] =
      div(
        button(onClick(Msg.Increment))(label),
        div(state.toString)
      )
