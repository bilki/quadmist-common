lazy val commonSettings = Seq(
  scalaVersion := "2.13.3",
  scalafmtAll in Compile := true,
  scalacOptions ++= Seq(
    "-Ymacro-annotations",
    "-language:higherKinds"
  )
)

lazy val `quadmist-common` = project
  .in(file("."))
  .aggregate(`quadmist-common-sub`.jvm, `quadmist-common-sub`.js)
  .settings(commonSettings)
  .settings(
    name := """quadmist-common""",
    version := "0.1.0-SNAPSHOT",
    organization := "com.lambdarat"
  )

lazy val `quadmist-common-sub` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .in(file("."))
  .jvmSettings(
    libraryDependencies ++= memeid ++ commonDependencies
  )
  .jvmSettings(commonSettings)
  .jsSettings(
    libraryDependencies ++= commonDependencies,
    scalaJSUseMainModuleInitializer := false
  )
  .jsSettings(commonSettings)

lazy val commonDependencies = Seq(newtype, scalacheck % Test) ++
  circe ++ enumeratum ++ cats ++ scalatest

addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.0" cross CrossVersion.full)
addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
