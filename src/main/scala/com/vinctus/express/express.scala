package com.vinctus

//import typings.express.mod.{RequestHandler => TypingsRequestHandler}
//import typings.expressServeStaticCore.mod.{ParamsDictionary, Query => TypingsQuery}

import scala.scalajs.js
import scala.scalajs.js.|

package object express {
//  type TRequestHandler = TypingsRequestHandler[ParamsDictionary, _, _, TypingsQuery]
  type Next = js.Function1[js.Any, Unit]
  type Query = typings.qs.mod.ParsedQs
  type RequestHandler = js.Function3[Request, Response, js.Dynamic, Unit]
  type ErrorHandler[E <: js.Object] = js.Function4[E, Request, Response, js.Dynamic, Unit]
  type PathParams = String | js.RegExp | Array[String | js.RegExp]
}
