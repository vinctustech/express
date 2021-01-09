package com.vinctus.express

import com.vinctus.oql.OQL

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global
//import typings.dotenv.mod.config
import typings.pg.mod.types.setTypeParser

import java.time.{Instant, Period}
import scala.scalajs.js

object Main extends App {

  val app = Express()
  val port = 3000

  app.get("/", (_, res, _) => res.send("Hello World!"))
  app.listen(port, () => println(s"Example app listening at http://localhost:$port"))

}

//object Main extends App {
//
//  val result = config()
//
//  if (result.error.isDefined) {
//    throw js.JavaScriptException(result.error.get)
//  }
//
//  setTypeParser(20, (s: Any) => s.asInstanceOf[String].toDouble)
//
//  val app = Express()
//  val port = 3000
//
//  val oqlconn: OQL = OQLConnection()
//
//  val handler =
//    requestHandler { req =>
//      async {
//        json(a = await(oqlconn.count("organization")),
//             b = await(oqlconn.count("organization [name like 'AAA%']")),
//             c = req.query.asdf)
//      }
//    }
//
//  app.get("/", handler)
//  app.listen(port, () => println(s"Example app listening at http://localhost:$port"))
//
//}
