val commonSettings = Seq(
  scalaVersion := "2.13.6",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.2.9"
  ),
)

val devices = project.settings(
  commonSettings,
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % "2.6.16"
  ),
)

val devices_subscriber = project.settings(commonSettings)

val root = project.in(file(".")).settings().aggregate(devices, devices_subscriber)
