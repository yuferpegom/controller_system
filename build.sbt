val akkaVersion = "2.6.16"

val commonSettings = Seq(
  scalaVersion := "2.13.6",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.2.9",
    "com.typesafe.akka" %% "akka-stream-kafka" % "2.1.1",
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb
      .compiler
      .Version
      .scalapbVersion % "protobuf",
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  ),
)

val common = project.settings(
  commonSettings,
  Compile / PB.targets := Seq(
    scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
  ),
)

val devices = project
  .settings(commonSettings)
  .dependsOn(common)

val slickVersion = "3.3.3"

val devices_subscriber = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % slickVersion,
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    ),
    assembly / mainClass := Some("org.cohesion.boot.Boot"),
  )
  .dependsOn(common)

val root = project.in(file(".")).settings().aggregate(common, devices, devices_subscriber)
