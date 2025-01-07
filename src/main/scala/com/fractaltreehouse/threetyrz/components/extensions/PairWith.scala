package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.Html
import tyrian.Cmd

// Combines two components into one, with the output of the new component being either the output of the first component or the output of the second component
// For example, multiple text boxes can be combined into a single component that emits the value of the text box that was most recently changed
class PairedWith[F[_], I, O, M, S, I2, O2, M2, S2](
    component: TyrianComponent2[F, I, O, M, S],
    component2: TyrianComponent2[F, I2, O2, M2, S2],
    combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
) extends TyrianComponent2[
      F,
      Either[I, I2],
      Either[O, O2],
      Either[M, M2],
      (S, S2)
    ] {
  def init: ((S, S2), Cmd[F, Either[M, M2]]) = {
    val (s1, c1) = component.init
    val (s2, c2) = component2.init
    val combined = c1.map(Left(_)) |+| c2.map(Right(_))
    ((s1, s2), combined)
  }
  def update(state: (S, S2), value: Either[Either[I, I2], Either[M, M2]]): (
      (S, S2),
      Cmd[F, Either[Either[O, O2], Either[M, M2]]]
  ) = {
    val (s, s2) = state
    value match {
      case Left(Left(i)) =>
        val (sb, cmd) = component.update(s, Left(i))
        val cmdb = cmd.map {
          case Left(o)  => Left(Left(o))
          case Right(m) => Right(Left(m))
        }
        ((sb, s2), cmdb)
      case Left(Right(i2)) =>
        val (s2b, cmd2) = component2.update(s2, Left(i2))
        val cmd2b = cmd2.map {
          case Left(o2)  => Left(Right(o2))
          case Right(m2) => Right(Right(m2))
        }
        ((s, s2b), cmd2b)
      case Right(Left(o)) =>
        val (sb, cmd) = component.update(s, Right(o))
        val cmdb = cmd.map {
          case Left(o)  => Left(Left(o))
          case Right(m) => Right(Left(m))
        }
        ((sb, s2), cmdb)
      case Right(Right(o2)) =>
        val (s2b, cmd2) = component2.update(s2, Right(o2))
        val cmd2b = cmd2.map {
          case Left(o2)  => Left(Right(o2))
          case Right(m2) => Right(Right(m2))
        }
        ((s, s2b), cmd2b)
    }
  }
  def view(state: (S, S2)): Html[Either[M, M2]] = {
    val (s, s2) = state
    val h1      = component.view(s)
    val h2      = component2.view(s2)
    combineUI(h1, h2)
  }
}
