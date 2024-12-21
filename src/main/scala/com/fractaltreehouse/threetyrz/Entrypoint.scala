package com.fractaltreehouse.threetyrz

import zio.interop.catz.*
import scala.scalajs.js.annotation.*
import com.fractaltreehouse.threetyrz.components.*
import examplecomponents.*

@JSExportTopLevel("ThreeTyrz")
object ThreeTyrz extends TyrianZIOComponentApp2(SideBySideExample2.component)
