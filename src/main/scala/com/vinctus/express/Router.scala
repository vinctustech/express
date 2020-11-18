package com.vinctus.express

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait Router extends RequestHandler {
  def delete(path: PathParams, handlers: RequestHandler*): Unit
  def get(path: PathParams, handlers: RequestHandler*): Unit
  def head(path: PathParams, handlers: RequestHandler*): Unit
  def patch(path: PathParams, handlers: RequestHandler*): Unit
  def post(path: PathParams, handlers: RequestHandler*): Unit
  def put(path: PathParams, handlers: RequestHandler*): Unit
}

@JSImport("express", "Router")
@js.native
object Router extends js.Object {
  def apply(): Router = js.native
}
