package com.fractaltreehouse.threejs.interop

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// todo: use this

@js.native
@JSImport("three/addons/capabilities/WebGL.js", JSImport.Default)
object WebGL extends js.Object {
  def isWebGL2Available(): Boolean = js.native
  def getWebGL2ErrorMessage(): String = js.native
}
