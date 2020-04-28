name := "snow-scraper"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.11"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.1" % Test,
  "org.apache.pdfbox" % "pdfbox" % "2.0.19"
)