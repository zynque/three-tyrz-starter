package com.fractaltreehouse.threetyrz

import com.fractaltreehouse.threejs.interop.*
import org.scalajs.dom
import zio.*

def makeScene(targetDiv: dom.Element) = {
  val width    = 400 // targetDiv.clientWidth
  val height   = 300 // targetDiv.clientHeight
  val scene    = new Scene()
  val camera   = new PerspectiveCamera(75, width / height, 0.1, 1000)
  val renderer = new WebGLRenderer()
  renderer.setSize(width, height)

  val geometry = new BoxGeometry(1, 1, 1);
  val material =
    MeshBasicMaterial.withParams(MeshBasicMaterialParameters(0x00ff00))
  val cube = new Mesh(geometry, material);
  scene.add( cube );
  camera.position.z = 5;

  targetDiv.appendChild(renderer.domElement)
  println("appended renderer div")

  renderer.render(scene, camera)
}

val threeDemoTask: Task[Msg] = ZIO.blocking {
  ZIO.attempt {
    val targetDiv = dom.document.getElementById("three-container")
    targetDiv match {
      case null => println("Could not find target div"); Msg.RetryInitThreeJS
      case div  => println("Found div"); makeScene(div); Msg.NoOp
    }
  }
}
