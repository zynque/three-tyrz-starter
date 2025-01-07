package com.fractaltreehouse.threetyrz.components.threejs

import zio.*
import com.fractaltreehouse.threejs.interop.*

// todo: pass message into update function and let the composition
//       update the model based on the message
trait ThreeJSComposition[Model, Elements <: {def camera: PerspectiveCamera}]:
  def initialModel: Model
  def initialize(scene: Scene, width: Int, height: Int, model: Model): Task[Elements]
  def update(scene: Scene, model: Model, elements: Elements): Task[Elements]
