import Dependencies._
import sbt.Keys.libraryDependencies

val coreNlpVersion = "3.9.2"
val sparkVersion = "2.4.0"

ThisBuild / scalaVersion := "2.11.12"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "fr.cnam"
ThisBuild / organizationName := "rcp216"

resolvers += Resolver.mavenLocal

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
assemblyJarName in assembly := s"${name.value}_${scalaBinaryVersion.value}-${sparkVersion}_${version.value}.jar"

lazy val root = (project in file("."))
  .settings(
    name := "racinetopic",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6" % "provided",
    libraryDependencies += "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.22",
    libraryDependencies += "org.apache.commons" % "commons-text" % "1.6",
    libraryDependencies += "fr.cnam" % "myopennlp" % "1.0.0",
    // libraryDependencies += "org.apache.lucene" % "lucene-core" % "7.7.1",
    // libraryDependencies += "com.databricks" % "spark-xml_2.11" % "0.5.0",

    libraryDependencies ++= {

      Seq(
        ("edu.stanford.nlp" % "stanford-corenlp" % coreNlpVersion).
          exclude("com.sun.xml.bind", "jaxb-impl"),
        "edu.stanford.nlp" % "stanford-corenlp" % coreNlpVersion classifier "models",
        "edu.stanford.nlp" % "stanford-corenlp" % coreNlpVersion classifier "models-french"
      ) ++
        Seq(
          "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
          "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
          "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
        )
    }

  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
