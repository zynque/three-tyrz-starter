package com.fractaltreehouse.threetyrz.components.threejs

import zio.*
import com.fractaltreehouse.threejs.interop.*

trait ThreeJSComposition[Model, Elements <: {def camera: PerspectiveCamera}]:
  def initialModel: Model
  def initialize(scene: Scene, width: Int, height: Int, model: Model): Task[Elements]
  def update(scene: Scene, model: Model, elements: Elements): Task[Elements]
