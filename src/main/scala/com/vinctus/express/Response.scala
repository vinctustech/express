package com.vinctus.express

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.|

@js.native
trait Response extends js.Object {
  val headersSent: Boolean = js.native
  val locals: js.Object = js.native
  val app: App = js.native
  def status(v: Int): Response = js.native
  def json(v: js.Any): Unit = js.native
  def send(v: String | js.Object | js.Array[js.Any]): Unit = js.native
  def sendStatus(s: Int): Unit = js.native
  def set(k: String, v: String): Unit = js.native
  def get(f: String): String = js.native
  @JSName("append")
  def appendMany(f: String, values: js.Array[String]): Unit = js.native
  def append(f: String, v: String): Unit = js.native
  def end(): Unit = js.native
  def attachment(a: String): Unit = js.native
  @JSName("attachment")
  def attachments(a: js.Array[String]): Unit = js.native
  def location(path: String): Unit = js.native
  def render(view: String, locals: js.Array[js.Any]): Unit = js.native
  def sendFile(path: String,
               options: js.Object,
               cb: js.Function2[Request, Response, js.Function1[_ <: js.Any, Unit]]): Unit = js.native
}
