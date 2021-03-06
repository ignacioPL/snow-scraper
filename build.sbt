name := "snow-scraper"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.11"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.apache.pdfbox" % "pdfbox" % "2.0.19",
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "com.github.pathikrit" %% "better-files" % "3.8.0",
  "org.jsoup" % "jsoup" % "1.13.1"
)