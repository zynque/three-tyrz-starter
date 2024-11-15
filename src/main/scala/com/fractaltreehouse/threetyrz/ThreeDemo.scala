package com.fractaltreehouse.threetyrz

import com.fractaltreehouse.threejs.interop.*
import org.scalajs.dom
import zio.*

case class SceneData(
    renderer: WebGLRenderer,
    scene: Scene,
    camera: PerspectiveCamera,
    cube: Mesh,
    angle: EulerAngle
)

def initializeScene(targetDiv: dom.Element) = {
  val width    = 400
  val height   = 400
  val scene    = new Scene()
  val camera   = new PerspectiveCamera(75, width / height, 0.1, 1000)
  val renderer = new WebGLRenderer()
  renderer.setSize(width, height)

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
  scene.add(ambientLight);

  camera.position.z = 5

  targetDiv.appendChild(renderer.domElement)

  renderer.render(scene, camera)

  SceneData(renderer, scene, camera, cube, EulerAngle(0, 0, 0))
}

val threeDemoTask: Task[Msg] = ZIO.blocking {
  ZIO.attempt {
    val targetDiv = dom.document.getElementById("three-container")
    targetDiv match {
      case null => println("Could not find target div"); Msg.RetryInitThreeJS
      case div =>
        println("Found div"); Msg.SceneInitialized(initializeScene(div))
    }
  }
}

def updateScene(model: Model): Task[Msg] = ZIO.attempt {
  model.sceneData match {
    case None => Msg.NoOp
    case Some(sceneData) =>
      val currentAngle = sceneData.angle
      val x            = currentAngle.x
      val y            = currentAngle.y
      val z            = currentAngle.z
      val yOffset = model.currentDirection match
        case RotationDirection.Left  => -0.02
        case RotationDirection.Right => 0.02
      val newAngle     = EulerAngle(x + 0.001, y + yOffset, z + 0.001)
      val newSceneData = sceneData.copy(angle = newAngle)
      Msg.RenderScene(newSceneData)
  }
}

def renderScene(sceneData: SceneData): Task[Msg] = ZIO.blocking {
  ZIO.attempt {
    val cube = sceneData.cube
    cube.rotation.x = sceneData.angle.x
    cube.rotation.y = sceneData.angle.y
    cube.rotation.z = sceneData.angle.z
    val scene  = sceneData.scene
    val camera = sceneData.camera
    sceneData.renderer.render(scene, camera)
    Msg.WaitForNextClockTick
  }
}

// idea - virtual 3d scene, analagous to virutal dom - diffing and updating?
