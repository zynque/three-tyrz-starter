package com.fractaltreehouse.threetyrz.components

import tyrian.*
export com.fractaltreehouse.threetyrz.components.extensions.TyrianComponentExtensions.*

// F: Effect Type (Cats Effect or ZIO Task)
// I: Input, O: Output, S: State/Model, M: Message
trait TyrianComponent[F[_], I, O, M, S] {
  def init: (S, Cmd[F, M])
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]])
  def view(state: S): Html[M]
}

// todo: read and re-read this: https://dev.to/dwayne/stateless-and-stateful-components-no-reusable-views-in-elm-2kg0
//       compare with what we're trying to do here
//
//       also compare/contrast using purely functional streams as in/out ports

// todo: can we accomplish something like the below for syntax?
//       (for example, leveraging something like eitheraccumulator)
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
