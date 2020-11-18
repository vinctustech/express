package com.vinctus.express

import typings.node.httpMod.Server

import scala.scalajs.js

@js.native
trait Application extends js.Object {
  def listen(port: Double, callback: js.Function0[Unit]): Server = js.native
  def use(middleware: js.Any): Unit
  def use(path: String, handlers: Handler*): Unit
  def get(path: String, handlers: Handler2*): Unit
}
