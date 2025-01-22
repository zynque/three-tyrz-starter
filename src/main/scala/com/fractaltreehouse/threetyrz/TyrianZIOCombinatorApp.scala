package com.fractaltreehouse.threetyrz

import zio.*
import cats.effect.kernel.Async
import org.zynque.tyriancombinators.elements.*
import tyrian.*

class TyrianZIOCombinatorApp[I, O, M, S](
    element: TyrianElement[Task, I, O, M, S]
)(using Async[Task])
    extends TyrianZIOApp[AppMessage[I, O, M], S] with TyrianCombinatorApp[Task, I, O, M, S](element) {
}
