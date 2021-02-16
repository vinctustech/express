package com.vinctus.express

import scala.scalajs.js
import scala.scalajs.js.|

@js.native
trait Request extends js.Object {
  val baseUrl: String = js.native
  val body: js.Dictionary[String] = js.native
  def get(field: String): js.UndefOr[String] = js.native
  val params: js.Dictionary[String] = js.native
  def asJsonObject[T <: js.Object]: T = js.native
  val originalUrl: String = js.native
  val path: String = js.native
  val protocol: String = js.native
  val secure: Boolean = js.native
  val cookies: js.Dictionary[String] = js.native
  def query: js.Dictionary[String] = js.native
  val hostname: String = js.native
  val ip: String = js.native
  val fresh: Boolean = js.native
  val stale: Boolean = js.native

  val method: String = js.native
  val url: String = js.native

  def accepts(): js.Array[String] = js.native
  def accepts(t: String): String | Boolean = js.native
  def accepts(t: js.Array[String]): String | Boolean = js.native
}
