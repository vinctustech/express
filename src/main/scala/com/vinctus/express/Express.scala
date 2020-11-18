package com.vinctus.express

import scala.scalajs.js

@js.native
trait Express extends Application {
  var request: Request = js.native
  var response: Response = js.native
}
