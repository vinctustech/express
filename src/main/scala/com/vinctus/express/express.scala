package com.vinctus

import typings.node.processMod.global.process

import java.time.{Instant, LocalDate}
import scala.collection.immutable.{AbstractMap, ListMap}
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.|
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.JSConverters._

package object express {
  type Next = js.Function1[js.Any, Unit]
  type RequestHandler = js.Function3[Request, Response, js.Dynamic, Any]
  type ErrorHandler[E <: js.Object] = js.Function4[E, Request, Response, js.Dynamic, Any]
  type PathParams = String | js.RegExp | Array[String | js.RegExp]

  object env extends Dynamic {
    def selectDynamic(field: String): String = process.env(field).get
  }

  def toJS(a: Any): js.Any =
    a match {
      case date: LocalDate  => new js.Date(date.getYear, date.getMonthValue - 1, date.getDayOfMonth)
      case instant: Instant => instant.toString
      case d: BigDecimal    => d.toDouble
      case d: Double        => d
      case n: Int           => n
      case b: Boolean       => b
      case null             => null
      case l: Seq[_]        => l map toJS toJSArray
      case m: Map[_, _] =>
        (m map { case (k, v) => k -> toJS(v) })
          .asInstanceOf[Map[String, Any]]
          .toJSDictionary
      case s => s.toString
    }

  def jsObject(v: Any): Boolean =
    js.typeOf(v) == "object" && (v != null) && !v.isInstanceOf[Long] && !v.isInstanceOf[js.Date] && !v
      .isInstanceOf[js.Array[_]]

  def toMap(obj: js.UndefOr[js.Any]): ListMap[String, Any] = {
    def toMap(obj: js.Any): ListMap[String, Any] = {
      var map: ListMap[String, Any] = obj.asInstanceOf[js.Dictionary[js.Any]].to(ListMap)

      for ((k, v) <- map)
        if (jsObject(v))
          map = map + ((k, toMap(v.asInstanceOf[js.Any])))

      map
    }

    if (!obj.isDefined) null
    else toMap(obj)
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
    object params extends Dynamic {
      def applyDynamic(method: String)(capture: Int): String = req.params(capture.toString)

      def selectDynamic(param: String): String = req.params(param)
    }

    object query extends Dynamic {
      def apply(): Map[String, Any] = toMap(req.query)

      def applyDynamic(method: String)(capture: Int): String = req.query(capture.toString)

      def selectDynamic(param: String): String = req.query(param)
    }
  }

  def asyncHandler(future: => Future[Result]): RequestHandler =
    (_, res: Response, next: js.Dynamic) =>
      future.andThen {
        case Success(Result(code, mime, body)) =>
          res.set("Content-Type", mime)
          res.status(code).send(body)
        case Failure(e) => next(e.getMessage)
      }.toJSPromise

  def asyncHandler(handler: SRequest => Future[Result]): RequestHandler =
    (req: Request, res: Response, next: js.Dynamic) =>
      handler(new SRequest(req)).andThen {
        case Success(Result(code, mime, body)) =>
          res.set("Content-Type", mime)
          res.status(code).send(body)
        case Failure(e) => next(e.getMessage)
      }.toJSPromise

  def requestHandler(result: => Result): RequestHandler = { (_, res: Response, next: js.Dynamic) =>
    try {
      result match {
        case Result(code, mime, body) =>
          res.set("Content-Type", mime)
          res.status(code).send(body)
      }
    } catch {
      case e: Exception => next(e.getMessage)
    }
  }

  def requestHandler(handler: SRequest => Result): RequestHandler = { (req: Request, res: Response, next: js.Dynamic) =>
    try {
      handler(new SRequest(req)) match {
        case Result(code, mime, body) =>
          res.set("Content-Type", mime)
          res.status(code).send(body)
      }
    } catch {
      case e: Exception => next(e.getMessage)
    }
  }

  case class Result(code: Int, mime: String, body: String)

  object json extends Dynamic {
    private def toJson(pairs: Seq[(String, Any)], tab: Int = 2, format: Boolean = false): String = {
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

      def jsonValue(value: Any): Unit = value match {
        case _: Double | _: Int | _: Boolean | null => buf ++= String.valueOf(value)
        case m: Map[_, _]                           => jsonObject(m.toSeq.asInstanceOf[Seq[(String, Any)]])
        case s: Seq[_] if s.isEmpty                 => buf ++= "[]"
        case s: Seq[_] =>
          buf += '['
          indent()
          aggregate(s)(jsonValue)
          dedent()
          buf += ']'
        case _ =>
          buf += '"'
          buf ++= value.toString
          buf += '"'
      }

      def jsonObject(pairs: Seq[(String, Any)]): Unit =
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

      jsonObject(pairs)
      buf.toString
    }

    def applyDynamicNamed(method: String)(properties: (String, Any)*): Result =
      Result(
        200,
        "application/json",
        toJson(properties)
      )
  }

  object map extends Dynamic {
    def applyDynamicNamed(method: String)(properties: (String, Any)*): DynamicMap =
      method match {
        case "apply" => new DynamicMap(properties.to(ListMap))
      }
  }

  class DynamicMap(obj: ListMap[String, Any]) extends AbstractMap[String, Any] with Dynamic {
    def selectDynamic(field: String): String = obj(field).asInstanceOf[String]

    def removed(key: String): DynamicMap = new DynamicMap(obj.removed(key))

    def get(key: String): Option[Any] = obj.get(key)

    def iterator: Iterator[(String, Any)] = obj.iterator

    def updated[V1 >: Any](key: String, value: V1): DynamicMap = new DynamicMap(obj.updated(key, value))

    override def toString: String = obj.map { case (k, v) => s"$k: $v" }.mkString("{", ", ", "}")
  }

}
