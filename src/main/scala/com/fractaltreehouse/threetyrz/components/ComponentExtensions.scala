package com.fractaltreehouse.threetyrz.components

import tyrian.Html
import tyrian.Cmd
import scala.sys.Prop

extension [F[_], I, O, M, S](component: TyrianComponent2[F, I, O, M, S]) {
  def mapOutput[O2](f: O => O2): TyrianComponent2[F, I, O2, M, S] = {
    new TyrianComponent2[F, I, O2, M, S] {
      override val init: (S, Cmd[F, M]) = component.init
      override def update(state: S, value: Either[I,M]): (S, Cmd[F, Either[O2, M]]) =
        val (state2, output) = component.update(state, value)
        val output2 = for {
          outOrMsg <- output
          transformed = outOrMsg.left.map(f) 
        } yield transformed
        (state2, output2)
      override def view(state: S): Html[M] = component.view(state)
    }
  }

  // horizontal composition: feed output of this component into another component
  def feedInto[O2, M2, S2](
    component2: TyrianComponent2[F, O, O2, M2, S2],
    combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
    ): TyrianComponent2[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] = {
    new TyrianComponent2[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] {
      override val init: ((S, S2), Cmd[F, CompositionMsg[O, M, M2]]) = {
        val (s1, c1) = component.init
        val (s2, c2) = component2.init
        val leftC1 = c1.map(CompositionMsg.Msg1(_))
        val rightC2 = c2.map(CompositionMsg.Msg2(_))
        val combined = leftC1 |+| rightC2
        ((s1, s2), combined)
      }
      override def update(state: (S, S2), value: Either[I, CompositionMsg[O, M, M2]]):
        (
          (S, S2),
          Cmd[F, Either[O2, CompositionMsg[O, M, M2]]]
        ) =
      {
        val (s, s2) = state
        value match {
          case Left(i) =>
            val (sb, cmd) = component.update(s, Left(i))
            val cmdb = cmd.map{
              case Left(o) => Right(CompositionMsg.Out(o))
              case Right(m) => Right(CompositionMsg.Msg1(m))
            }
            ((sb, s2), cmdb)
          case Right(CompositionMsg.Out(o)) =>
            val (s2b, cmd2) = component2.update(s2, Left(o))
            val cmd2b = cmd2.map {
              case Left(o2) => Left(o2)
              case Right(m2) => Right(CompositionMsg.Msg2(m2))
            }
            ((s, s2b), cmd2b)
          case Right(CompositionMsg.Msg1(m)) =>
            val (sb, cmd) = component.update(s, Right(m))
            val cmdb = cmd.map{
              case Left(o) => Right(CompositionMsg.Out(o))
              case Right(m) => Right(CompositionMsg.Msg1(m))
            }
            ((sb, s2), cmdb)
          case Right(CompositionMsg.Msg2(m2)) =>
            val (s2b, cmd2) = component2.update(s2, Right(m2))
            val cmd2b = cmd2.map {
              case Left(o2) => Left(o2)
              case Right(m2) => Right(CompositionMsg.Msg2(m2))
            }
            ((s, s2b), cmd2b)
        }
      }
      override def view(state: (S, S2)): Html[CompositionMsg[O, M, M2]] = {
        val (s, s2) = state
        val h1 = component.view(s)
        val h2 = component2.view(s2)
        combineUI(h1, h2).map {
          case Left(m) => CompositionMsg.Msg1(m)
          case Right(m2) => CompositionMsg.Msg2(m2)
        }
      }
    }
  }

  // does not display component2 - use for downstream components that do stateful data transformations but have no ui
  def feedInto[O2, M2, S2](
    component2: TyrianComponent2[F, O, O2, M2, S2]
    ): TyrianComponent2[F, I, O2, CompositionMsg[O, M, M2], (S, S2)] =
    feedInto(component2, (h1, h2) => h1.map(Left(_)))

  def pairWith[I2, O2, M2, S2](
    component2: TyrianComponent2[F, I2, O2, M2, S2],
    combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
    ):
    TyrianComponent2[F, Either[I, I2], Either[O, O2], Either[M, M2], (S, S2)] = {
    new TyrianComponent2[F, Either[I, I2], Either[O, O2], Either[M, M2], (S, S2)] {
      override val init: ((S, S2), Cmd[F, Either[M, M2]]) = {
        val (s1, c1) = component.init
        val (s2, c2) = component2.init
        val combined = c1.map(Left(_)) |+| c2.map(Right(_))
        ((s1, s2), combined)
      }
      override def update(state: (S, S2), value: Either[Either[I, I2], Either[M, M2]]):
        (
          (S, S2),
          Cmd[F, Either[Either[O, O2], Either[M, M2]]]
        ) =
      {
        val (s, s2) = state
        value match {
          case Left(Left(i)) =>
            val (sb, cmd) = component.update(s, Left(i))
            val cmdb = cmd.map{
              case Left(o) => Left(Left(o))
              case Right(m) => Right(Left(m))
            }
            ((sb, s2), cmdb)
          case Left(Right(i2)) =>
            val (s2b, cmd2) = component2.update(s2, Left(i2))
            val cmd2b = cmd2.map {
              case Left(o2) => Left(Right(o2))
              case Right(m2) => Right(Right(m2))
            }
            ((s, s2b), cmd2b)
          case Right(Left(o)) =>
            val (sb, cmd) = component.update(s, Right(o))
            val cmdb = cmd.map{
              case Left(o) => Left(Left(o))
              case Right(m) => Right(Left(m))
            }
            ((sb, s2), cmdb)
          case Right(Right(o2)) =>
            val (s2b, cmd2) = component2.update(s2, Right(o2))
            val cmd2b = cmd2.map {
              case Left(o2) => Left(Right(o2))
              case Right(m2) => Right(Right(m2))
            }
            ((s, s2b), cmd2b)
        }
      }
      override def view(state: (S, S2)): Html[Either[M, M2]] = {
        val (s, s2) = state
        val h1 = component.view(s)
        val h2 = component2.view(s2)
        combineUI(h1, h2)
      }      
    }
  }

  def propagateState: TyrianComponent2[F, I, S, M, S] = {
    new TyrianComponent2[F, I, S, M, S] {
      override val init: (S, Cmd[F, M]) = component.init
      override def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[S, M]]) = {
        val (s2, cmd) = component.update(state, value)
        val cmd2 = cmd.map {
          case Left(o) => Left(s2)
          case Right(m) => Right(m)
        }
        (s2, cmd2)
      }

      override def view(state: S): Html[M] = component.view(state)
    }
  }
}
