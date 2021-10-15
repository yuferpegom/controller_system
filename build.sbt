val commonSettings = Seq(
  scalaVersion := "2.13.6",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.2.9",
    "com.typesafe.akka" %% "akka-stream-kafka" % "2.1.1",
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb
      .compiler
      .Version
      .scalapbVersion % "protobuf",
  ),
)

val akkaVersion = "2.6.16"

val common = project.settings(
  commonSettings,
  Compile / PB.targets := Seq(
    scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
  ),
)

val devices = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    ),
  )
  .dependsOn(common)

val devices_subscriber = project.settings(commonSettings)

val root = project.in(file(".")).settings().aggregate(common, devices, devices_subscriber)
