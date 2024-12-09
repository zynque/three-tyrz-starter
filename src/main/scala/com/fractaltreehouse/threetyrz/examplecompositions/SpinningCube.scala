package com.fractaltreehouse.threetyrz.examplecompositions

import com.fractaltreehouse.threejs.interop.*
import zio.*
import com.fractaltreehouse.threetyrz.components.threejs.*

enum RotationDirection:
  case Left
  case Right
  
case class SpinningCubeElements(
    camera: PerspectiveCamera,
    cube: Mesh,
    currentAngle: EulerAngle
)

// unsafe/impure tasks that manipulate the three.js scene and camera
// todo: further isolate the impure scene manipulation
//       a pure scene update should take a pure data model representing the scene
//       and output a copy of that data model representing the updated scene
object SpinningCubeComposition
    extends ThreeJSComposition[RotationDirection, SpinningCubeElements] {

  val initialModel = RotationDirection.Left

  def initialize(scene: Scene, width: Int, height: Int, model: RotationDirection) = ZIO.attempt {

    val geometry = new BoxGeometry(1, 1, 1)
    val material =
      MeshStandardMaterial.withParams(MeshStandardMaterialParameters(0x00ff00))
    val cube = new Mesh(geometry, material)
    scene.add(cube)

    val light = new PointLight(0xffffff, 1000)
    light.position.x = 10
    light.position.y = 10
    light.position.z = 10
    scene.add(light)

    val ambientLight = new AmbientLight(0x404040, 2)
    scene.add(ambientLight)

    val camera = new PerspectiveCamera(75, width / height, 0.1, 1000)
    camera.position.z = 5

    SpinningCubeElements(camera, cube, EulerAngle(0, 0, 0))
  }

  def update(
      scene: Scene,
      model: RotationDirection,
      elements: SpinningCubeElements
  ): Task[SpinningCubeElements] = ZIO.attempt {
    val currentAngle = elements.currentAngle
    val x            = currentAngle.x
    val y            = currentAngle.y
    val z            = currentAngle.z
    val yOffset = model match
      case RotationDirection.Left  => -0.02
      case RotationDirection.Right => 0.02
    val newAngle    = EulerAngle(x + 0.001, y + yOffset, z + 0.001)
    val newElements = elements.copy(currentAngle = newAngle)
    val cube        = elements.cube
    cube.rotation.x = newAngle.x
    cube.rotation.y = newAngle.y
    cube.rotation.z = newAngle.z
    newElements
  }
}
