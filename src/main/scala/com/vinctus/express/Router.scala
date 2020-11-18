package com.vinctus.express

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait Router extends RequestHandler {
  def delete(path: PathParams, handlers: RequestHandler*): Router
  def get(path: PathParams, handlers: RequestHandler*): Router
  def head(path: PathParams, handlers: RequestHandler*): Router
  def patch(path: PathParams, handlers: RequestHandler*): Router
  def post(path: PathParams, handlers: RequestHandler*): Router
  def put(path: PathParams, handlers: RequestHandler*): Router
}

@JSImport("express", "Router")
@js.native
object Router extends js.Object {
  def apply(): Router = js.native
}
