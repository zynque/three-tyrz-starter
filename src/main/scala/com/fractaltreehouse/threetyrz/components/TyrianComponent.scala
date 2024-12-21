package com.fractaltreehouse.threetyrz.components

import tyrian.*
import tyrian.Html.*
import cats.effect.kernel.Async
import zio.*
import cats.*
import cats.syntax.functor.*

// I: Input, O: Output, S: State/Model, M: Message
trait TyrianComponent[F[_], S, M] {
  def init(): (S, Cmd[F, M])
  def update(state: S, message: M): (S, Cmd[F, M])
  def view(state: S): Html[M]
}


// object CombineSignalsExample {
//   case class User(name: String, age: Int, address: String)

//   val userForm = BuildUI {
//     val name = NameUI.build
//     val age = AgeUI.build
//     val address = AddressUI.build

//     val buildUser = for {
//       n <- name.signal
//       a <- age.signal
//       ad <- address.signal
//     } yield User(n, a, ad)

//     val ui = div(
//       name.ui,
//       age.ui,
//       address.ui
//     )
//   }

//   // other libraries in this style include:
//   // - Slinky
//   // - Scala.js React
  
// }
