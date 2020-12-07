package com.vinctus.express

import typings.node.httpMod.Server

import scala.scalajs.js
import scala.scalajs.js.Dictionary
import scala.scalajs.js.annotation.JSName

@js.native
trait Application extends js.Object {
  val locals: Dictionary[js.Any]

  def listen(port: Double, callback: js.Function0[Unit]): Server = js.native
  @JSName("use")
  def useHandler(handler: js.Function3[_, _, _, _]): Unit
  def use(handler: RequestHandler): Unit
  def use[E <: js.Object](handler: ErrorHandler[E]): Unit
  def use(path: PathParams, handler: RequestHandler*): Unit
  def delete(path: PathParams, handlers: RequestHandler*): Unit
  def get(path: PathParams, handlers: RequestHandler*): Unit
  def head(path: PathParams, handlers: RequestHandler*): Unit
  def patch(path: PathParams, handlers: RequestHandler*): Unit
  def post(path: PathParams, handlers: RequestHandler*): Unit
  def put(path: PathParams, handlers: RequestHandler*): Unit
}
