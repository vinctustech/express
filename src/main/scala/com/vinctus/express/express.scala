package com.vinctus

import scala.scalajs.js
import scala.scalajs.js.|

package object express {
  type Next = js.Function1[js.Any, Unit]
  type Query = typings.qs.mod.ParsedQs
  type RequestHandler = js.Function3[Request, Response, js.Dynamic, Any]
  type ErrorHandler[E <: js.Object] = js.Function4[E, Request, Response, js.Dynamic, Any]
  type PathParams = String | js.RegExp | Array[String | js.RegExp]
}
