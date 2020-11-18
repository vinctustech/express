package com.vinctus.express

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait Routable extends js.Object {
  def useTotal(handler: TotalHandler): Unit = js.native
  @JSName("use")
  def useOnPathTotal(p: PathParams, handler: TotalHandler): Unit = js.native
  @JSName("use")
  def useOnPathWithManyTotal(p: PathParams, handlers: js.Array[TotalHandler]): Unit = js.native
  def use(handler: Handler): Unit = js.native
  @JSName("use")
  def useOnPath(p: PathParams, handler: Handler): Unit = js.native
  @JSName("use")
  def useOnPathWithMany(p: PathParams, handlers: js.Array[Handler]): Unit = js.native
}
