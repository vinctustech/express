express
=======

*express* is a Scala.js facade and interface layer for the "express" NPM package.

Example
-------

```scala
import com.vinctus.express.Express

object Main extends App {

  val app = Express()
  val port = 3000

  app.get("/", (_, res, _) => res.send("Hello World!"))
  app.listen(port, () => println(s"Example app listening at http://localhost:$port"))

}
```