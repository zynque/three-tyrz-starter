package com.fractaltreehouse.threetyrz

import tyrian.*
import tyrian.Html.*
import zio.*
import org.scalajs.dom
import com.fractaltreehouse.threejs.interop.*
import scala.reflect.Selectable.reflectiveSelectable

enum ThreeJSDivMsg[+CompositionModel, +CompositionElements]:
  case NoOp
  case FindTargetDiv
  case RetryFindTargetDiv
  case InitializeScene(div: dom.Element)
  case SceneInitialized(renderer: WebGLRenderer, scene: Scene, model: CompositionModel, elements: CompositionElements)
  case UpdateComposition
  case Updated(elements: CompositionElements)
  case Render
  case WaitForNextClockTick
  // case ClockTick // -- from outside?

case class ThreeJSDivComponentState[CompositionModel, CompositionElements](
  model: CompositionModel,
  threeJSData: Option[(WebGLRenderer, Scene, CompositionElements)] = None)

class ThreeJSDivComponent[CompositionModel, CompositionElements <: {def camera: PerspectiveCamera}](
    width: Int,
    height: Int,
    composition: ThreeJSComposition[CompositionModel, CompositionElements]
) extends ZIOTyrianComponent[Unit, Nothing, ThreeJSDivComponentState[CompositionModel, CompositionElements], ThreeJSDivMsg[CompositionModel, CompositionElements] | UnexpectedError]:
  def init(i: Unit): (ThreeJSDivComponentState[CompositionModel, CompositionElements], Cmd[Task, ThreeJSDivMsg[CompositionModel, CompositionElements] | UnexpectedError]) =
    val initialModel = composition.initialModel
    (ThreeJSDivComponentState(initialModel, None), ZIO.succeed(ThreeJSDivMsg.FindTargetDiv).toCommand)
  def update(
      state: ThreeJSDivComponentState[CompositionModel, CompositionElements],
      message: ThreeJSDivMsg[CompositionModel, CompositionElements] | UnexpectedError
  ): (ThreeJSDivComponentState[CompositionModel, CompositionElements], Cmd[Task, ThreeJSDivMsg[CompositionModel, CompositionElements] | UnexpectedError]) = message match {
    case ThreeJSDivMsg.NoOp => (state, Cmd.None)
    case ThreeJSDivMsg.FindTargetDiv =>
      (state, findTargetDiv.toCommand)
    case ThreeJSDivMsg.RetryFindTargetDiv =>
      (state, ZIO.succeed(ThreeJSDivMsg.FindTargetDiv).delay(10.millis).toCommand)
    case ThreeJSDivMsg.InitializeScene(div) =>
      (state, initializeScene(div, width, height, composition).toCommand)
    case ThreeJSDivMsg.SceneInitialized(renderer, scene, model, elements) =>
      val threeJSData = (renderer, scene, elements)
      (state.copy(model = model, threeJSData = Some(threeJSData)), ZIO.succeed(ThreeJSDivMsg.Render).toCommand)
    case ThreeJSDivMsg.Render =>
      state.threeJSData match {
        case None => (state, ZIO.succeed(UnexpectedError("render message recieved with no three.js data available")).toCommand)
        case Some((renderer, scene, elements)) =>
          (state, render(renderer, scene, elements).toCommand)
      }
    case ThreeJSDivMsg.UpdateComposition =>
      state.threeJSData match {
        case None => (state, ZIO.succeed(UnexpectedError("update composition message recieved with no three.js data available")).toCommand)
        case Some((renderer, scene, elements)) =>
          (state, updateComposition(composition, scene, state.model, elements).toCommand)
      }
    case ThreeJSDivMsg.Updated(elements) =>
      (state.copy(threeJSData = state.threeJSData.map{case (renderer, scene, _) => (renderer, scene, elements)}), ZIO.succeed(ThreeJSDivMsg.Render).toCommand)
    case ThreeJSDivMsg.WaitForNextClockTick =>
      (state, ZIO.succeed(ThreeJSDivMsg.UpdateComposition).delay(10.millis).toCommand)
    case UnexpectedError(msg) =>
      (state, ZIO.attempt{println("error: " + msg); ThreeJSDivMsg.NoOp}.toCommand)
  }

  def view(state: ThreeJSDivComponentState[CompositionModel, CompositionElements]): Html[Nothing] =
    div(
      id := "three-container"
    )() // todo: generate unique ids so we can have multiple instances of this component

def updateComposition[CompositionModel, CompositionElements <: {def camera: PerspectiveCamera}](
    composition: ThreeJSComposition[CompositionModel, CompositionElements],
    scene: Scene,
    model: CompositionModel,
    elements: CompositionElements
): Task[ThreeJSDivMsg[CompositionModel, CompositionElements]] =
  for {
    updatedElements <- composition.update(scene, model, elements)
  } yield ThreeJSDivMsg.Updated(updatedElements)

def render[CompositionModel, CompositionElements <: {def camera: PerspectiveCamera}](
    renderer: WebGLRenderer,
    scene: Scene,
    elements: CompositionElements
): Task[ThreeJSDivMsg[CompositionModel, CompositionElements]] =
  ZIO.attempt(renderer.render(scene, elements.camera)).as(ThreeJSDivMsg.WaitForNextClockTick)

val findTargetDiv: Task[ThreeJSDivMsg[Nothing, Nothing]] = ZIO.blocking {
  ZIO.attempt {
    val targetDiv = dom.document.getElementById("three-container") // todo: generate unique ids
    targetDiv match {
      case null =>
        println("Could not find target div"); ThreeJSDivMsg.RetryFindTargetDiv
      case div =>
        println("Found div"); ThreeJSDivMsg.InitializeScene(div)
    }
  }
}

def initializeScene[CompositionModel, CompositionElements <: {def camera: PerspectiveCamera}](
    targetDiv: dom.Element,
    width: Int,
    height: Int,
    composition: ThreeJSComposition[CompositionModel, CompositionElements]
) = {

  def initializeScene = ZIO.attempt {
    val scene = new Scene()
    scene
  }

  def initializeRenderer = ZIO.attempt {
    val renderer = new WebGLRenderer()
    renderer.setSize(width, height)
    targetDiv.appendChild(renderer.domElement)
    renderer
  }

  for {
    renderer  <- initializeRenderer
    scene <- initializeScene
    model = composition.initialModel
    elements <- composition.initialize(scene, width, height, model)
  } yield ThreeJSDivMsg.SceneInitialized(renderer, scene, model, elements)
}
