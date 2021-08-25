import Dependencies._

//ThisBuild / scalaVersion := "2.13.5"
ThisBuild / scalaVersion := "3.0.1"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"
ThisBuild / scalacOptions ++= Seq()

lazy val commonSettings = Seq(
  target := {
    baseDirectory.value / "target"
  },
//  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.6.1",
    "org.typelevel" %% "cats-effect" % "3.1.1",
    "org.tpolecat" %% "doobie-core" % "1.0.0-M5",
    "org.tpolecat" %% "doobie-hikari" % "1.0.0-M5",
    "org.tpolecat" %% "doobie-postgres" % "1.0.0-M5"
  )
)

lazy val dddUtil = (project in file("ddd-util"))
  .settings(
    name := "tweetable-ddd",
    commonSettings
  )

lazy val tweetableEntities = (project in file("tweetable-entities"))
  .settings(
    name := "tweetable-entities",
    commonSettings,
    libraryDependencies ++= Seq(
      "eu.timepit" %% "refined" % "0.9.26"
    )
  )
  .dependsOn(dddUtil)

lazy val tweetableUseCase = (project in file("tweetable-usecases"))
  .settings(
    name := "tweetable-usecases",
    commonSettings
  )
  .dependsOn(dddUtil, tweetableEntities)

lazy val tweetableInterface = (project in file("tweetable-interface"))
  .settings(
    name := "tweetable-interface",
    commonSettings
  )
  .dependsOn(dddUtil, tweetableUseCase)

lazy val tweetableDriver = (project in file("tweetable-driver"))
  .settings(
    name := "tweetable-driver",
    commonSettings
  )
  .dependsOn(dddUtil, tweetableInterface)

lazy val evolutions = (project in file("evolutions"))
  .settings(
    name := "evolutions",
    commonSettings,
    libraryDependencies ++= Seq(
      ("com.typesafe.play" %% "play-jdbc" % "2.8.8").cross(CrossVersion.for3Use2_13),
      ("com.typesafe.play" %% "play-jdbc-evolutions" % "2.8.8").cross(CrossVersion.for3Use2_13),
      "mysql" % "mysql-connector-java" % "8.0.26"
    )
  )

//todo:  そのうち必要になりそうなlibrary 後で整理する

//libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.0"
//libraryDependencies += "org.typelevel" %% "cats-effect" % "3.1.1"
//
//libraryDependencies += "org.http4s" %% "http4s-core" % "1.0-234-d1a2b53"
//libraryDependencies += "org.http4s" %% "http4s-blaze-server" % "1.0-234-d1a2b53"
//
//val circeVersion = "0.14.1"
//libraryDependencies ++= Seq(
//  "io.circe" %% "circe-core",
//  "io.circe" %% "circe-generic",
//  "io.circe" %% "circe-parser"
//).map(_ % circeVersion)
//
//libraryDependencies += "dev.profunktor" %% "redis4cats-streams" % "1.0.0-RC3"
//
//libraryDependencies ++= Seq(
//  // Start with this one
//  "org.tpolecat" %% "doobie-core"      % "0.12.1",
//
//  // And add any of these as needed
//  "org.tpolecat" %% "doobie-hikari"    % "0.12.1",          // HikariCP transactor.
//  "org.tpolecat" %% "doobie-postgres"  % "0.12.1",          // Postgres driver 42.2.19 + type mappings.
//)
//
//libraryDependencies += "io.github.kirill5k" %% "mongo4cats-core" % "0.2.9"
//libraryDependencies += "io.github.kirill5k" %% "mongo4cats-circe" % "0.2.9" // circe support
