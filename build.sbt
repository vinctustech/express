name := "express"

version := "0.1.0-snapshot.57"

scalaVersion := "2.13.4"

scalacOptions ++= Seq( "-deprecation", "-feature", "-unchecked", "-language:postfixOps", "-language:implicitConversions", "-language:existentials", "-language:dynamics", "-Xasync")

organization := "com.vinctus"

githubOwner := "vinctustech"

githubRepository := "oql"

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += "Hyperreal Repository" at "https://dl.bintray.com/edadma/maven"

externalResolvers += "OQL" at "https://maven.pkg.github.com/vinctustech/oql"

enablePlugins(ScalaJSPlugin)

enablePlugins(ScalablyTypedConverterPlugin)

//enablePlugins(ScalablyTypedConverterGenSourcePlugin)
//
//stMinimize := Selection.All
//
//stOutputPackage := "com.vinctus.express.facade"

Test / scalaJSUseMainModuleInitializer := true

Test / scalaJSUseTestModuleInitializer := false

jsEnv := new org.scalajs.jsenv.nodejs.NodeJSEnv()

libraryDependencies ++= Seq(
//  "org.scala-lang.modules" %%% "scala-xml" % "1.2.0"
)

npmDependencies in Compile ++= Seq(
  "@types/node" -> "14.14.10"
//  "@types/express" -> "4.17.9",

)

libraryDependencies ++= Seq(
  "org.scalatest" %%% "scalatest" % "3.1.1" % "test"
  //"org.scalacheck" %%% "scalacheck" % "1.14.1" % "test"
)

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-async" % "0.10.0",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
)

libraryDependencies ++= Seq(
  "com.vinctus" %%% "sjs-utils" % "0.1.0-snapshot.14",
  "io.github.cquiroz" %%% "scala-java-time" % "2.0.0" % "test"
)

mainClass in (Compile, run) := Some( s"${organization.value}.${name.value.replace('-', '_')}.Main" )

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

licenses := Seq("ISC" -> url("https://opensource.org/licenses/ISC"))

homepage := Some(url("https://github.com/edadma/" + name.value))

pomExtra :=
  <scm>
    <url>git@github.com:edadma/{name.value}.git</url>
    <connection>scm:git:git@github.com:edadma/{name.value}.git</connection>
  </scm>
  <developers>
    <developer>
      <id>edadma</id>
      <name>Edward A. Maxedon, Sr.</name>
      <url>https://github.com/edadma</url>
    </developer>
  </developers>
