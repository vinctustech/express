package com.vinctus.express

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
trait Express extends Application {
  var request: Request = js.native
  var response: Response = js.native
}

@JSImport("express", JSImport.Namespace)
@js.native
object Express extends js.Object {
  def apply(): Express = js.native
}
