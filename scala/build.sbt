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

// https://stackoverflow.com/questions/42605598/sbt-how-to-dockerize-a-fat-jar
assemblyMergeStrategy in assembly := {
  case x => {
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    val strategy = oldStrategy(x)
    if (strategy == MergeStrategy.deduplicate)
      MergeStrategy.first
    else strategy
  }
}

// Remove all jar mappings in universal and append the fat jar
mappings in Universal := {
  val universalMappings = (mappings in Universal).value
  val fatJar = (assembly in Compile).value
  val filtered = universalMappings.filter {
    case (file, name) => !name.endsWith(".jar")
  }
  filtered :+ (fatJar -> ("lib/" + fatJar.getName))
}
