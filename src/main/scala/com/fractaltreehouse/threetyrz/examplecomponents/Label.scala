package com.fractaltreehouse.threetyrz.examplecomponents

import tyrian.*
import tyrian.Html.*
import zio.*
import com.fractaltreehouse.threetyrz.components.*

object Label:
  type State = String
  type Input = String
  
  class Component(initialText: State)
      extends SimpleConsumerComponent[Input, State]:

    def initSimple = initialText

    def updateSimple(state: State, input: Input): State =
      input

    def view(state: State): Html[Nothing] =
      div(state)
