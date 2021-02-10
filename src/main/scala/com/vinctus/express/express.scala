package com.vinctus

import typings.node.processMod.global.process

import java.time.{Instant, LocalDate}
import scala.collection.immutable.{AbstractMap, ListMap}
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.{Dictionary, |}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.JSConverters._
import com.vinctus.sjs_utils.Implicits._
import com.vinctus.sjs_utils.toMap

package object express {
  type Next = js.Function1[js.Any, Unit]
  type RequestHandler = js.Function3[Request, Response, js.Dynamic, Any]
  type ErrorHandler[E <: js.Object] = js.Function4[E, Request, Response, js.Dynamic, Any]
  type PathParams = String | js.RegExp | Array[String | js.RegExp]

  object env extends Dynamic {
    def selectDynamic(field: String): String = process.env(field).get
  }

//  class JSResult(val result: Any) extends js.Object
//
//  def asyncHandler[T, R](f: T => R, res: Response, next: js.Dynamic)(future: => Future[T]): js.Promise[_] =
//    future.andThen {
//      case Success(r) => res.json(new JSResult(f(r)))
//      case Failure(e) => next(e.getMessage)
//    }.toJSPromise
//
//  def asyncHandler(res: Response, next: js.Dynamic)(future: => Future[Any]): js.Promise[Any] =
//    future.andThen {
//      case Success(r) => res.json(new JSResult(r))
//      case Failure(e) => next(e.getMessage)
//    }.toJSPromise
//
//  def asyncHandler(future: => Future[Any]): RequestHandler =
//    (_, res: Response, next: js.Dynamic) =>
//      future.andThen {
//        case Success(r) => res.json(new JSResult(r))
//        case Failure(e) => next(e.getMessage)
//      }.toJSPromise

  class SRequest(req: Request) {
    def body: Dictionary[js.Any] = req.body

    object params extends Dynamic {
      def applyDynamic(method: String)(capture: Int): String = req.params(capture.toString)

      def selectDynamic(param: String): String = req.params(param)
    }

    object query extends Dynamic {
      def apply(): Map[String, Any] = toMap(req.query)

      def obj: js.Object = req.query.asInstanceOf[js.Object]

      def applyDynamic(method: String)(capture: Int): String = req.query(capture.toString)

      def selectDynamic(param: String): String = req.query(param)
    }
  }

  private def send(res: Response, result: Result): Unit =
    result match {
      case Result(code, None, None)       => res.sendStatus(code)
      case Result(code, None, Some(body)) => res.status(code).send(body)
      case Result(code, Some(mime), Some(body)) =>
        res.set("Content-Type", mime)
        res.status(code).send(body)
      case Result(_, Some(_), None) => sys.error("can't have a content-type header with no content")
    }

  def asyncHandler(future: => Future[Result]): RequestHandler =
    (_, res: Response, next: js.Dynamic) =>
      future.andThen {
        case Success(r) => send(res, r)
        case Failure(e) => next(e.getMessage)
      }.toJSPromise

  def asyncHandler(handler: SRequest => Future[Result]): RequestHandler =
    (req: Request, res: Response, next: js.Dynamic) =>
      handler(new SRequest(req)).andThen {
        case Success(r) => send(res, r)
        case Failure(e) => next(e.getMessage)
      }.toJSPromise

  def requestHandler(result: => Result): RequestHandler = { (_, res: Response, next: js.Dynamic) =>
    try {
      send(res, result)
    } catch {
      case e: Exception => next(e.getMessage)
    }
  }

  def requestHandler(handler: SRequest => Result): RequestHandler = { (req: Request, res: Response, next: js.Dynamic) =>
    try {
      send(res, handler(new SRequest(req)))
    } catch {
      case e: Exception => next(e.getMessage)
    }
  }

  case class Result(code: Int, mime: Option[String], body: Option[String])

  def json(pairs: Seq[(String, Any)], tab: Int = 2, format: Boolean = false): String = {
    val buf = new StringBuilder
    var level = 0

    def ln(): Unit =
      if (format)
        buf += '\n'

    def indent(): Unit = {
      ln()
      level += tab
      margin()
    }

    def dedent(): Unit = {
      ln()
      level -= tab
      margin()
    }

    def margin(): Unit =
      if (format)
        buf ++= " " * level

    def aggregate[T](seq: Seq[T])(render: T => Unit): Unit = {
      val it = seq.iterator

      if (it.nonEmpty)
        render(it.next())

      while (it.hasNext) {
        buf += ','
        ln()
        margin()
        render(it.next())
      }
    }

    def jsonValue(value: Any): Unit =
      value match {
        case _: Double | _: Int | _: Boolean | null => buf ++= String.valueOf(value)
        case m: Map[_, _]                           => jsonObject(m.toSeq.asInstanceOf[Seq[(String, Any)]])
        case s: Seq[_] if s.isEmpty                 => buf ++= "[]"
        case s: Seq[_] =>
          buf += '['
          indent()
          aggregate(s)(jsonValue)
          dedent()
          buf += ']'
        case a: js.Array[_] => jsonValue(a.toList)
        case p: Product     => jsonObject(p.productElementNames zip p.productIterator toList)
        case _: String | _: Instant =>
          buf += '"'
          buf ++= value.toString.replace("\\", "\\\\").replace("\"", "\\\"")
          buf += '"'
        case d: js.Date =>
          buf += '"'
          buf ++= d.toISOString()
          buf += '"'
        case o: js.Object => jsonObject(o.asInstanceOf[js.Dictionary[js.Any]].toList)
      }

    def jsonObject(pairs: Seq[(String, Any)]): Unit = {
      if (pairs.isEmpty)
        buf ++= "{}"
      else {
        buf += '{'
        indent()
        aggregate(pairs) {
          case (k, v) =>
            jsonValue(k)
            buf ++= (if (format) ": " else ":")
            jsonValue(v)
        }
        dedent()
        buf += '}'
      }
    }

    jsonObject(pairs)
    buf.toString
  }

  object response extends Dynamic {
    def enacted: Result =
      Result(
        HTTP.NO_CONTENT,
        None,
        None
      )

    def applyDynamicNamed(method: String)(properties: (String, Any)*): Result =
      method match {
        case "apply" =>
          Result(
            HTTP.OK,
            Some("application/json"),
            Some(json(properties))
          )
      }
  }

}
