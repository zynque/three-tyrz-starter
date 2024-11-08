package com.fractaltreehouse.threejs.interop

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSImport("three", "Scene")
class Scene extends js.Object {
  def add(obj: js.Object): Unit = js.native
}

@js.native
@JSImport("three", "PerspectiveCamera")
class PerspectiveCamera(
    fieldOfView: Double,
    aspectRatio: Double,
    nearPlane: Double,
    farPlane: Double
) extends js.Object {
  def position: Vector3 = js.native
}

@js.native
@JSImport("three", "Vector3")
class Vector3(var x: Double, var y: Double, var z: Double) extends js.Object

@js.native
@JSImport("three", "WebGLRenderer")
class WebGLRenderer extends js.Object {
  def setSize(width: Double, height: Double): Unit = js.native
  def domElement: org.scalajs.dom.Element          = js.native
  def render(scene: js.Object, camera: js.Object): Unit = js.native
}

@js.native
@JSImport("three", "BoxGeometry")
class BoxGeometry(width: Double, height: Double, depth: Double)
    extends js.Object

@js.native
@JSImport("three", "MeshBasicMaterial")
class MeshBasicMaterial(parameters: js.Object) extends js.Object

case class MeshBasicMaterialParameters(color: Int)

object MeshBasicMaterial {
  def withParams(params: MeshBasicMaterialParameters): MeshBasicMaterial =
    new MeshBasicMaterial(js.Dynamic.literal(color = params.color))
}

@js.native
@JSImport("three", "Mesh")
class Mesh(geometry: js.Object, material: js.Object) extends js.Object
