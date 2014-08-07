name := "eKanban"

version := "1.0-SNAPSHOT"

resolvers := Seq("typesafe" at "http://repo.typesafe.com/typesafe/releases/")

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "commons-codec" % "commons-codec" % "1.7",
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4”,
  "com.newrelic.agent.java" % "newrelic-agent" % “3.9.0
)     

play.Project.playScalaSettings
