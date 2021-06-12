import Dependencies._

ThisBuild / scalaVersion     := "2.13.5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val commonSettings = Seq(
  target := { baseDirectory.value / "target" },
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.3.0",
    "org.typelevel" %% "cats-effect" % "3.1.1"
  )
)

lazy val dddUtil = (project in file("ddd-util"))
  .settings(
    name := "tweetable-ddd"
  )

lazy val tweetableEntities = (project in file("tweetable-entities"))
  .settings(
    name := "tweetable-entities",
    commonSettings,
  )
  .dependsOn(dddUtil)





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

