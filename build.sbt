lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """play-scala-jwt-psql-docker-starter""",
    version := "1.0",
    maintainer := "info@datumbrain.com",
    scalaVersion := "2.12.1",
    libraryDependencies ++= Seq(
      guice,
      "com.typesafe.play" %% "play-slick" % "4.0.2",
      "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
      "org.postgresql" % "postgresql" % "42.2.9",
      "org.mindrot" % "jbcrypt" % "0.4",
      "com.typesafe.play" % "play-mailer_2.12" % "6.0.1",
      "com.typesafe.play" % "play-mailer-guice_2.12" % "6.0.1",
      "com.pauldijou" %% "jwt-play" % "4.2.0"
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )
