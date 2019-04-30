import Dependencies._

ThisBuild / scalaVersion := "2.11.12"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "fr.cnam"
ThisBuild / organizationName := "rcp216"

lazy val root = (project in file("."))
  .settings(
    name := "racinetopic",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.22",
    libraryDependencies += "org.apache.commons" % "commons-text" % "1.6"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
