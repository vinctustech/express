addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.3.1")

resolvers += Resolver.bintrayRepo("oyvindberg", "converter")

addSbtPlugin("org.scalablytyped.converter" % "sbt-converter" % "1.0.0-beta29.1")

resolvers += Resolver.bintrayRepo("edadma", "sbt-plugins")

addSbtPlugin("xyz.hyperreal" % "npm-plugin" % "0.1.13")

addSbtPlugin("com.codecommit" % "sbt-github-packages" % "0.5.2")
