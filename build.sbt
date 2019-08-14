name := "advanced-scala-with-cats"

version := "0.1"

scalaVersion := "2.12.9"

libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.1"

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-Ypartial-unification"
)
