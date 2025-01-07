package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.Html
import tyrian.Cmd

enum CompositionMsg[+O, +M, +M2]:
    case Out(o: O)
    case Msg1(m: M)
    case Msg2(m: M2)

// Feeds the output of one component into the input of another, while maintaining internal state for each
// For example, a button that emits click events can be fed into a logical counter component, and the result can be fed into a display component
class FedInto[F[_], I, O, M, S, O2, M2, S2](
    component: TyrianComponent[F, I, O, M, S],
    component2: TyrianComponent[F, O, O2, M2, S2],
    combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
) extends TyrianComponent[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] {
  def init: ((S, S2), Cmd[F, CompositionMsg[O, M, M2]]) = {
    val (s1, c1) = component.init
    val (s2, c2) = component2.init
    val leftC1   = c1.map(CompositionMsg.Msg1(_))
    val rightC2  = c2.map(CompositionMsg.Msg2(_))
    val combined = leftC1 |+| rightC2
    ((s1, s2), combined)
  }
  def update(
      state: (S, S2),
      value: Either[I, CompositionMsg[O, M, M2]]
  ): ((S, S2), Cmd[F, Either[O2, CompositionMsg[O, M, M2]]]) = {
    val (s, s2) = state
    value match {
      case Left(i) =>
        val (sb, cmd) = component.update(s, Left(i))
        val cmdb = cmd.map {
          case Left(o)  => Right(CompositionMsg.Out(o))
          case Right(m) => Right(CompositionMsg.Msg1(m))
        }
        ((sb, s2), cmdb)
      case Right(CompositionMsg.Out(o)) =>
        val (s2b, cmd2) = component2.update(s2, Left(o))
        val cmd2b = cmd2.map {
          case Left(o2)  => Left(o2)
          case Right(m2) => Right(CompositionMsg.Msg2(m2))
        }
        ((s, s2b), cmd2b)
      case Right(CompositionMsg.Msg1(m)) =>
        val (sb, cmd) = component.update(s, Right(m))
        val cmdb = cmd.map {
          case Left(o)  => Right(CompositionMsg.Out(o))
          case Right(m) => Right(CompositionMsg.Msg1(m))
        }
        ((sb, s2), cmdb)
      case Right(CompositionMsg.Msg2(m2)) =>
        val (s2b, cmd2) = component2.update(s2, Right(m2))
        val cmd2b = cmd2.map {
          case Left(o2)  => Left(o2)
          case Right(m2) => Right(CompositionMsg.Msg2(m2))
        }
        ((s, s2b), cmd2b)
    }
  }
  def view(state: (S, S2)): Html[CompositionMsg[O, M, M2]] = {
    val (s, s2) = state
    val h1      = component.view(s)
    val h2      = component2.view(s2)
    combineUI(h1, h2).map {
      case Left(m)   => CompositionMsg.Msg1(m)
      case Right(m2) => CompositionMsg.Msg2(m2)
    }
  }
}
