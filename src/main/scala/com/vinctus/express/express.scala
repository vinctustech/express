package com.vinctus

import java.time.{Instant, LocalDate}
import scala.collection.immutable.ListMap
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

  class JSResult(val result: Any) extends js.Object

  def asyncHandler[T, R](f: T => R, res: Response, next: js.Dynamic)(future: => Future[T]): js.Promise[_] =
    future.andThen {
      case Success(r) => res.json(new JSResult(f(r)))
      case Failure(e) => next(e.getMessage)
    }.toJSPromise

  def asyncHandler(res: Response, next: js.Dynamic)(future: => Future[Any]): js.Promise[Any] =
    future.andThen {
      case Success(r) => res.json(new JSResult(r))
      case Failure(e) => next(e.getMessage)
    }.toJSPromise

  def asyncHandler(future: => Future[Any]): RequestHandler =
    (_, res: Response, next: js.Dynamic) =>
      future.andThen {
        case Success(r) => res.json(new JSResult(r))
        case Failure(e) => next(e.getMessage)
      }.toJSPromise

  def requestHandler(future: => Future[Any]): RequestHandler =
    (_, res: Response, next: js.Dynamic) =>
      future.andThen {
        case Success(r) => res.json(new JSResult(toJS(r)))
        case Failure(e) => next(e.getMessage)
      }.toJSPromise

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

  def requestHandler(handler: SRequest => Future[Result]): RequestHandler =
    (req: Request, res: Response, next: js.Dynamic) =>
      handler(new SRequest(req)).andThen {
        case Success(r) => res.json(new JSResult(toJS(r)))
        case Failure(e) => next(e.getMessage)
      }.toJSPromise

  case class Result(status: Int, mime: String, body: String)

  object json extends Dynamic {
    def applyDynamicNamed(method: String)(properties: (String, Any)*): String = properties.map{case (k, v) => s"  \"$k\": $v"}.mkString("{\n", ",\n", "}\n")
  }

}
