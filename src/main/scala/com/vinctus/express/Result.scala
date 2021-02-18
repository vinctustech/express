package com.vinctus.express

case class Result(code: Int, mime: Option[String], body: Option[String]) {
  def send(res: Response): Unit =
    this match {
      case Result(code, None, None)       => res.sendStatus(code)
      case Result(code, None, Some(body)) => res.status(code).send(body)
      case Result(code, Some(mime), Some(body)) =>
        res.set("Content-Type", mime)
        res.status(code).send(body)
      case Result(_, Some(_), None) => sys.error("can't have a content-type header with no content")
    }
}

object response extends Dynamic {
  def enacted: Result =
    Result(
      HTTP.NO_CONTENT,
      None,
      None
    )
  def badRequest(error: String = "bad request"): Result =
    Result(
      HTTP.BAD_REQUEST,
      Some("application/json"),
      Some(json(List("error" -> error)))
    )
  def internalServerError(error: String = "internal server error"): Result =
    Result(
      HTTP.INTERNAL_SERVER_ERROR,
      Some("application/json"),
      Some(json(List("error" -> error)))
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
