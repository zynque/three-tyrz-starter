package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*

object Label:
  type State = String

  enum Msg:
    case Updated(text: String)
  
  class Component(initialText: State)
      extends SimpleOutComponent[Msg, State]:

    def init = (initialText, Cmd.None)

    def updateSimple(state: State, message: Msg): (State, Cmd[Task, Msg]) =
      message match
        case Msg.Updated(text) => (text, Cmd.None)

    def view(state: State): Html[Msg] =
      div(state)
