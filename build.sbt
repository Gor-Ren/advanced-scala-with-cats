name := "advanced-scala-with-cats"

version := "0.1"

scalaVersion := "2.12.9"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.+"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.+" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.+" % "test"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification"
)
