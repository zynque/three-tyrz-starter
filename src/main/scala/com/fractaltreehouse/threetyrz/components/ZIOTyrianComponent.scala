package com.fractaltreehouse.threetyrz.components

import tyrian.*
import tyrian.Html.*
import cats.effect.kernel.Async
import zio.*

trait ZIOTyrianComponent[S, M] extends TyrianComponent[Task, S, M]
