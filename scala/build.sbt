import sbtassembly.AssemblyPlugin.autoImport._

name := """hello-world"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.196"
// https://stackoverflow.com/questions/46450250/play-framework-dev-server-shutting-down-after-refreshing
libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.1"

test in assembly := {}

assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}
