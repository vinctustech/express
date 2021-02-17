package com.vinctus.express

import com.vinctus.sjs_utils.toMap

import scala.scalajs.js

class SRequest(req: Request) {
  def bodyMap: js.Dictionary[String] = req.body
  def body: js.Dynamic = req.body.asInstanceOf[js.Dynamic]

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
