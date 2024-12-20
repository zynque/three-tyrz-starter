package com.fractaltreehouse.threetyrz.components

enum CompositionMsg[+O, +M, +M2]:
    case Out(o: O)
    case Msg1(m: M)
    case Msg2(m: M2)
