package com.fractaltreehouse.threetyrz.components.extensions

import com.fractaltreehouse.threetyrz.components.*
import tyrian.Html
import tyrian.Cmd

enum SwitcherSelection:
    case One
    case Two

case class SwitcherState[S1, S2](selection: SwitcherSelection, state1: S1, state2: S2)

// switches between two components based on signal from a third
// class Switcher[F[_], I, O, MC, MS, M1, S1, M2, S2](
//     currentChoice: TyrianComponent[F, Any, Boolean, MC, MS],
//     component1: TyrianComponent[F, I, O, M1, S1],
//     component2: TyrianComponent[F, I, O, M2, S2],
//     combineUI: (Html[M1], Html[M2]) => Html[Either[M1, M2]]
// ) extends TyrianComponent[F, I, O, Either[M1, M2], SwitcherState[S1, S2]] {
//   def init: (SwitcherState[S1, S2], Cmd[F, Either[M1, M2]]) = {
//     val (s1, c1) = component1.init
//     val (s2, c2) = component2.init
//     val leftC1   = c1.map(Left(_))
//     val rightC2  = c2.map(Right(_))
//     val commands = leftC1 |+| rightC2
//     (SwitcherState(SwitcherSelection.One, s1, s2), commands)
//   }
//   def update(
//       state: SwitcherState[S1, S2],
//       value: Either[I, Either[M1, M2]]
//   ): ((S1, S2), Cmd[F, Either[O, Either[M1, M2]]]) =
//     value match {
//       case Left(i) =>
//         state.selection match {
//           case SwitcherSelection.One =>
//             val (s1, cmd) = component1.update(state.state1, Left(i))
//             val cmdb = cmd.map {
//               case Left(o)  => Left(o)
//               case Right(m) => Right(Left(m))
//             }
//             ((s1, state.state2), cmdb)
//           case SwitcherSelection.Two =>
//             val (s2, cmd) = component2.update(state.state2, Left(i))
//             val cmdb = cmd.map {
//               case Left(o)  => Left(o)
//               case Right(m) => Right(Right(m))
//             }
//             ((state.state1, s2), cmdb)
//         }
//       case Right(Left(m1)) =>
//         val (s1, cmd) = component1.update(state.state1, Right(m1))
//         val cmdb = cmd.map {
//           case Left(o)  => Left(o)
//           case Right(m) => Right(Left(m))
//         }
//         ((s1, state.state2), cmdb)
//       case Right(Right(m2)) =>
//         val (s2, cmd) = component2.update(state.state2, Right(m2))
//         val cmdb = cmd.map {
//           case Left(o)  => Left(o)
//           case Right(m) => Right(Right(m))
//         }
//         ((state.state1, s2), cmdb)
//     }
//   def view(state: (S1, S2)): Html[Either[M1, M2]] = {
//     val (s1, s2) = state
//     val h1       = component1.view(s1)
//     val h2       = component2.view(s2)
//     combineUI(h1, h2)
//   }
// }
