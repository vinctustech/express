package com.vinctus.express

object Main extends App {

  val app = App()
  val port = 3000

  app.get("/", (_, res) => res.send("Hello World!"))
  app.listen(port, () => println(s"Example app listening at http://localhost:$port"))

}
