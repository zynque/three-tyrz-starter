package com.fractaltreehouse.threetyrz

import tyrian.Html.*
import tyrian.*
import tyrian.CSS.*
import zio.*
import zio.interop.catz.*
import scala.scalajs.js.annotation.*
import com.fractaltreehouse.threejs.interop.EulerAngle

@JSExportTopLevel("ThreeTyrz")
object ThreeTyrz extends TyrianZIOComponentApp(DemoAppComponent(), AppMsg.NoOp)
