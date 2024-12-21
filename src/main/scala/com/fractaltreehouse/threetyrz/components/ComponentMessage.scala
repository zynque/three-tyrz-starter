package com.fractaltreehouse.threetyrz.components

enum ComponentMessage[+I, +O, +M] {
  case Input(i: I)
  case Output(o: O)
  case InternalMessage(m: M)
  case None
}
