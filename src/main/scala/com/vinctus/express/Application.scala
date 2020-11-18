package com.vinctus.express

import typings.node.httpMod.Server

import scala.scalajs.js
import scala.scalajs.js.Dictionary

@js.native
trait Application extends js.Object {
  val locals: Dictionary[js.Any]

  def listen(port: Double, callback: js.Function0[Unit]): Server = js.native
  def use(middleware: js.Any): Unit
  def use(path: String, handlers: RequestHandler*): Unit
  def get(path: String, handlers: RequestHandler*): Unit
}
