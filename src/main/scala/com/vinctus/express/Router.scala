package com.vinctus.express

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait Router extends Handler {
  def get(path: String, handlers: Handler*): Unit
}

@JSImport("express", "Router")
@js.native
object Router extends js.Object {
  def apply(): Router = js.native
}
