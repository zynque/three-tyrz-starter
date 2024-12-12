package com.fractaltreehouse.threetyrz.components

import tyrian.*
import tyrian.Html.*
import cats.effect.kernel.Async
import zio.*

trait ZIOTyrianComponent[I, O, S, M] extends TyrianComponent[Task, I, O, S, M]
