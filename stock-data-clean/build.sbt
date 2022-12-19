lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.jacobleon",
      scalaVersion := "2.13.10"
    )),
    name := "stock-data-clean"
  )
 
libraryDependencies += "org.scala-lang" % "scala-library" % "2.13.10"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.39.4.1"