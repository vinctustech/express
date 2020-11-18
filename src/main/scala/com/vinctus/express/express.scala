package com.vinctus

import scala.scalajs.js
import scala.scalajs.js.|

package object express {
  type Next = js.Function1[js.UndefOr[_ <: js.Any | String], Unit]
  type RequestHandler = js.Function3[Request, Response, Next, Unit]
  type ErrorHandler[E <: js.Object] = js.Function4[E, Request, Response, Next, Unit]
  type PathParams = String | js.RegExp | Array[String | js.RegExp]
}
